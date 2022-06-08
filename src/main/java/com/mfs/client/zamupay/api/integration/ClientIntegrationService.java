package com.mfs.client.zamupay.api.integration;

import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.SSLContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.mfs.client.zamupay.api.dto.MFSResponse;
import com.mfs.client.zamupay.api.dto.TransactionResponse;
import com.mfs.client.zamupay.api.service.ConfigService;
import com.mfs.client.zamupay.exception.InvalidTokenException;
import com.mfs.client.zamupay.infrastucture.MFSConstants;
import com.mfs.client.zamupay.persistence.AccessTokenRepository;
import com.mfs.client.zamupay.persistence.EventLogRepository;
import com.mfs.client.zamupay.persistence.TransactionRepository;
import com.mfs.client.zamupay.persistence.model.AccessToken;
import com.mfs.client.zamupay.persistence.model.EventLog;
import com.mfs.client.zamupay.persistence.model.TransactionLog;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Class implements integration with Client REST API for:
 * <ul>
 *  <li>Creating Remittance request</li>
 *  <li>Checking status of Remittance request</li>
 *  <li>KYC inquiry for beneficiary</li>
 * </ul>
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class ClientIntegrationService {

    final ConfigService configService;
    final EventLogRepository eventLogRepository;
    final TransactionRepository transactionRepository;
	final AccessTokenRepository tokenRepository;

    private final ObjectMapper mapper = new ObjectMapper();
    private RestTemplate template;
    
    private EventLog eventLog;


	/**
	 * Invokes partner to get the authentication details
	 *
	 * @return accessToken object
	 */
	public AccessToken getAccessToken() {
		try{
			String tokenUrl = configService.getConfigByKey(MFSConstants.TOKEN_URL);
			log.info("Token Request URL: {}",tokenUrl);
			template = getRestTemplate(tokenUrl);

			final HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
			requestBody.add("client_id", configService.getConfigByKey(MFSConstants.CLIENT_ID));
			requestBody.add("client_secret", configService.getConfigByKey(MFSConstants.CLIENT_SECRET));
			requestBody.add("grant_type", configService.getConfigByKey(MFSConstants.GRANT_TYPE));
			requestBody.add("scope", configService.getConfigByKey(MFSConstants.SCOPE));

			HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, header);
			log.info("Token Request: {}",formEntity.toString());
			ResponseEntity<String> response = template.exchange(tokenUrl, HttpMethod.POST, formEntity, String.class);
			log.info("Token Request response: {}", response.getBody());

			JsonNode jsonNode = mapper.readTree(response.getBody());
			//Log the token
			Calendar calendar = Calendar.getInstance();
			AccessToken accessToken = new AccessToken();
			accessToken.setAccessToken(jsonNode.at("/access_token").asText());
			accessToken.setCreatedOn(calendar.getTime());
			calendar.add(Calendar.HOUR, 1);
			accessToken.setExpiresIn(calendar.getTime());
			accessToken.setTokenType(jsonNode.at("/token_type").asText());
			accessToken.setScope(jsonNode.at("/scope").asText());

			return tokenRepository.save(accessToken);

		}catch (HttpStatusCodeException exception){
			String responseMessage = exception.getResponseBodyAsString();
			if (StringUtils.isEmpty(responseMessage)) {
				HttpStatus status = HttpStatus.valueOf(exception.getRawStatusCode());
				responseMessage = status.value() + " " + status.getReasonPhrase();
			}
			log.info("Token Request error response {}", responseMessage);
			throw new InvalidTokenException(exception.getStatusCode().value(), responseMessage, exception.getCause());
		}catch (Exception exception){
			log.info("Token Request error response: {}", exception.getMessage());
			throw new InvalidTokenException(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), exception.getCause());
		}
	}

	/**
	 * Initiating bank client money transfer request
	 *
	 * @param transactionLog represents transaction entity
	 * @return bank client transaction response
	 */
    public MFSResponse<TransactionResponse> submitTransaction(TransactionLog transactionLog) {
        try {
			AccessToken token = tokenRepository.getByCreatedDateBetween(new Date()).orElseGet(this::getAccessToken);

			// Prepare JSON request for bank end-point
            String jsonRequest = preparePaymentRequest(transactionLog);

            // Create log in event log
            eventLog = eventLogRepository.save(createEventLog(transactionLog.getMfsReferenceId(), MFSConstants.MONEY_TRANSFER_SERVICE, jsonRequest));

            // Invoke bank client money transfer end-point
            String paymentOrderUrl = configService.getConfigByKey(MFSConstants.PAYMENT_ORDER_URL);
            log.info("Money transfer URL: {}",paymentOrderUrl);
            template=getRestTemplate(paymentOrderUrl);

			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization","Bearer "+token.getAccessToken());
            HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);
            
            String httpRequest = mapper.writeValueAsString(request);
			log.info("Money transfer request for {}: {}", transactionLog.getMfsReferenceId(), request.toString());
            ResponseEntity<String> response = template.postForEntity(paymentOrderUrl, request, String.class);
            log.info("Money transfer response for {}: {}", transactionLog.getMfsReferenceId(), response.getBody());

			//Update response in Event Log
			eventLogRepository.save(updateEventLog(eventLog, response.getBody()));

			TransactionResponse transactionResponse = mapper.readValue(response.getBody(),TransactionResponse.class);
			//update Transaction Log
			transactionRepository.save(updateTransactionLog(transactionLog,transactionResponse,MFSConstants.MONEY_TRANSFER_SERVICE));

			MFSResponse<TransactionResponse> mfsResponse = new MFSResponse<>();
			mfsResponse.setStatusCode(HttpStatus.OK.value());
			mfsResponse.setResponse(transactionResponse);
			return mfsResponse;

        } catch (HttpStatusCodeException httpStatusCodeException) {
			String responseMessage = httpStatusCodeException.getResponseBodyAsString();
			if(StringUtils.isEmpty(responseMessage)){
				HttpStatus status = HttpStatus.valueOf(httpStatusCodeException.getRawStatusCode());
				responseMessage = status.value()+" "+status.getReasonPhrase();
			}
        	log.info("Money transfer error response for {}: {}", transactionLog.getMfsReferenceId(), responseMessage);
            return handleErrorResponse(transactionLog, httpStatusCodeException);

        } catch (Exception exception) {
            log.info("Money transfer error response for {}: {}", transactionLog.getMfsReferenceId(), exception.getCause());
            return handleErrorResponse(transactionLog, exception);
        }
    }
	/**
	 * Invoking bank client end-point for query transaction status
	 *
	 * @param transactionLog represents transaction log
	 * @return bank client query transaction response
	 */
	public MFSResponse<TransactionResponse> queryTransaction(final TransactionLog transactionLog) {
		try {
			// Create log in event log
			eventLog = eventLogRepository.save(createEventLog(transactionLog.getMfsReferenceId(), MFSConstants.QUERY_SERVICE, transactionLog.getMfsReferenceId()));

			String queryTransactionURL = MessageFormat.format(configService.getConfigByKey(MFSConstants.QUERY_SERVICE_URL),	transactionLog.getMfsReferenceId());
			log.info("Get Status URL: {}",queryTransactionURL);
			template=getRestTemplate(queryTransactionURL);
			HttpEntity<String> request = new HttpEntity<>(initClientHeaders());
			
			String httpRequest = mapper.writeValueAsString(request);
			log.info("Get Status request for {}: {}", transactionLog.getMfsReferenceId(),httpRequest);

			// Invoke bank client transaction status end-point
			ResponseEntity<TransactionResponse> response = template.getForEntity(queryTransactionURL, TransactionResponse.class, request);
			log.info("Get Status response for {}: {}", transactionLog.getMfsReferenceId(), response.getBody());
			return processResponse(transactionLog, response.getBody());

		} catch (HttpStatusCodeException httpStatusCodeException) {
			String responseMessage = httpStatusCodeException.getResponseBodyAsString();
			if(StringUtils.isEmpty(responseMessage)){
				HttpStatus status = HttpStatus.valueOf(httpStatusCodeException.getRawStatusCode());
				responseMessage = status.value()+" "+status.getReasonPhrase();
			}
			log.info("Get Status response for {}: {}", transactionLog.getMfsReferenceId(),
					responseMessage);
			return handleErrorResponse(transactionLog, httpStatusCodeException);

		} catch (Exception exception) {
			log.info("Get Status response for {}: {}", transactionLog.getMfsReferenceId(), exception.getMessage());
			return handleErrorResponse(transactionLog, exception);
		}
	}

    // set default HTTP headers
    private HttpHeaders initClientHeaders() {
    	final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
    }

    /**
     * Prepare JSON request for bank Money transfer end-point
     *
     * @param transaction represents the transaction log that has all the
     *                       attributes needed by the transaction log
     * @return represents a JSON String
     * @throws JsonProcessingException
     */
	private String preparePaymentRequest(TransactionLog transaction) throws JsonProcessingException {
		return "";
	}

	/**
	 * Process and updates success response into event log and transaction log tables
	 *
	 * @param transactionLog
	 * @param transactionResponse
	 * @return
	 * @throws JsonProcessingException
	 */
	private MFSResponse<TransactionResponse> processResponse(TransactionLog transactionLog, TransactionResponse transactionResponse) throws JsonProcessingException {
		MFSResponse<TransactionResponse> bankResponse = new MFSResponse<>();

		// Update response in event log table
		eventLog.setResponse(mapper.writeValueAsString(transactionResponse));
        eventLog.setDateLogged(new Date());
		eventLogRepository.save(eventLog);

		// Update response in transaction log table
//		transactionLog.setResultCode(transactionResponse.getResponseCode());
//		transactionLog.setResultDesc(transactionResponse.getResponseDesc());
		transactionRepository.save(transactionLog);

		bankResponse.setResponse(transactionResponse);
		bankResponse.setStatusCode(HttpStatus.OK.value());
		return bankResponse;
	}

	/**
	 * Updates error response into event log table
	 *
	 * @param transactionLog represents transaction log
	 * @param exception represents HTTP client exception
	 * @return
	 */
	private MFSResponse<TransactionResponse> handleErrorResponse(TransactionLog transactionLog,	Exception exception) {
		int statusCode;
		String message;
		if (exception instanceof HttpStatusCodeException) {
			HttpStatusCodeException httpException = (HttpStatusCodeException) exception;
			try {
				Map<String, Object> errorResponse = mapper.readValue(httpException.getResponseBodyAsString(), new TypeReference<Map<String, Object>>() {});
				if (errorResponse!=null && errorResponse.containsKey("error") | "invalid_token".equals(String.valueOf(errorResponse.get("error"))))
					throw new InvalidTokenException(httpException.getMessage());
				else {
					statusCode = httpException.getStatusCode().value();
					message = errorResponse.containsKey("response")?String.valueOf(errorResponse.get("response")): String.valueOf(errorResponse.get("errorFriendlyMessgage"));
				}
			} catch (Exception e) {
				HttpStatus httpStatus = HttpStatus.valueOf(httpException.getRawStatusCode());
				statusCode = httpStatus.value();
				message = httpStatus.getReasonPhrase();
			}
		} else if (exception.getCause() instanceof SocketTimeoutException) {
			statusCode = HttpStatus.REQUEST_TIMEOUT.value();
			message = exception.getLocalizedMessage();

		} else if (exception.getCause() instanceof ConnectTimeoutException || exception.getCause() instanceof HttpHostConnectException) {
			statusCode = HttpStatus.GATEWAY_TIMEOUT.value();
			message = exception.getLocalizedMessage();
		}  else {
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
			message = exception.getLocalizedMessage();
		}

		MFSResponse<TransactionResponse> transferResponse = new MFSResponse<>();
		transferResponse.setStatusCode(statusCode);
		transferResponse.setResponse(TransactionResponse.builder()
				.mfsReferenceId(transactionLog.getMfsReferenceId())
				.build());
		return transferResponse;
	}

    /**
     * Logging request into Event log
     *
     * @param serviceName    represents name of the service
     * @param jsonRequest    represents the Client JSON request
     * @param mfsReferenceId represents MFS reference id that will be sent from the
     *                       MFS API
     * @return returns an object of eventLog
     */
    private EventLog createEventLog(String mfsReferenceId, String serviceName, String jsonRequest) {
        return EventLog.builder().dateLogged(new Date()).mfsReferenceId(mfsReferenceId).request(jsonRequest).serviceName(serviceName).build();
    }

	/**
	 * Updating transactionLog & transactionDetail with additional fields from the remittance API
	 *
	 * @param transactionLog      created log to be updated
	 * @param response from the client remittance API
	 * @return transaction log object
	 */
	private TransactionLog updateTransactionLog(TransactionLog transactionLog, Object response, String serviceName) {
		return TransactionLog.builder().build();
	}

		/**
         * Updating an existing event log
         *
         * @param eventLog represents the event log that will be updated
         * @param response represents the response obtained after invoking the client service
         */
	private EventLog updateEventLog(EventLog eventLog, String response) {
		eventLog.setResponse(response);
		eventLog.setDateLogged(new Date());
		return eventLog;
	}
    
    /**
     * Create RestTemplate with HTTP or HTTPS based on URL
     * @param url
     * @return a URL-based RestTemplate
     * @throws Exception
     */
	private RestTemplate getRestTemplate(String url) throws Exception {
		
		String urlPattern = "^(http|https|ftp)://.*$";
		if (url.matches(urlPattern)) {
			final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;

		    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
		                    .loadTrustMaterial(null, acceptingTrustStrategy)
		                    .build();

		    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		    CloseableHttpClient httpClient = HttpClients.custom()
		                    .setSSLSocketFactory(csf)
		                    .build();

		    HttpComponentsClientHttpRequestFactory requestFactory =
		                    new HttpComponentsClientHttpRequestFactory();

			int timeout = configService.getIntConfigByKey(MFSConstants.TIMEOUT);

			requestFactory.setHttpClient(httpClient);
			requestFactory.setConnectTimeout(timeout);
			requestFactory.setReadTimeout(timeout);
			requestFactory.setConnectionRequestTimeout(timeout);
		    return new RestTemplate(requestFactory);
		} 
		
	    return new RestTemplate();
	}

}
