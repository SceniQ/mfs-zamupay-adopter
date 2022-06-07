package com.mfs.client.zamupay.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfs.client.zamupay.api.dto.MFSResponse;
import com.mfs.client.zamupay.api.dto.TransactionResponse;
import com.mfs.client.zamupay.api.service.ConfigService;
import com.mfs.client.zamupay.persistence.EventLogRepository;
import com.mfs.client.zamupay.persistence.TransactionRepository;
import com.mfs.client.zamupay.persistence.model.EventLog;
import com.mfs.client.zamupay.persistence.model.SystemConfig;
import com.mfs.client.zamupay.persistence.model.TransactionLog;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.mfs.client.zamupay.infrastucture.MFSConstants.QUERY_SERVICE_URL;
import static com.mfs.client.zamupay.infrastucture.MFSConstants.SUBMIT_TRANSACTION_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpResponse.response;

/**
 * Tests the client integration service implementation
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-junit.properties")
public class ClientIntegrationServiceTest {

	@Autowired
	ClientIntegrationService integrationService;

	private static ClientAndServer mockServer;
	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private ConfigService configService;
	@MockBean
	private EventLogRepository eventLogRepository;
	@MockBean
	private TransactionRepository transactionRepository;

	/**
	 * Initializing the mock server
	 */
	@BeforeClass
	public static void startServer() {
		mockServer = startClientAndServer(8080);
	}

	/**
	 * Stopping the mock server after test run
	 */
	@AfterClass
	public static void stopServer() {
		mockServer.stop();
	}

	/**
	 * Initializing the web application context for testing
	 */
	@Before
	public void setUp() {
		when(eventLogRepository.save(any(EventLog.class))).thenReturn(EventLog.builder().eventId(1L).build());
		when(configService.getConfigByKey(SUBMIT_TRANSACTION_URL)).thenReturn("http://localhost:8080/mpambaservice/v1/B2CTransfer");
		when(configService.getConfigByKey(QUERY_SERVICE_URL)).thenReturn("http://localhost:8080/mpambaservice/v1/CheckB2CTransaction");
		when(transactionRepository.findByMfsReferenceId(any(String.class))).thenReturn(Optional.empty());
		when(transactionRepository.save(any(TransactionLog.class))).thenReturn(transactionLog());
	}


	/**
	 * Success test for calling the client submit transaction end-point URL
	 */
	@Test
	public void test_submit_transaction() throws Exception{
		TransactionResponse response = TransactionResponse.builder()
				.mfsReferenceId("MFS20210902001")
				.transactionId("BANK20210902001")
				.amount(200.00D)
				.primaryAccountNumber("+2547110000")
				.responseCode(0)
				.responseDesc("CREATED")
				.build();

		TransactionLog transactionLog = transactionLog();

		String clientRequest = "";

		new MockServerClient("localhost", 8080)
				.when(
						HttpRequest.request("/mpambaservice/v1/B2CTransfer")
								.withMethod("POST")
								.withBody(mapper.writeValueAsString(clientRequest), Charset.defaultCharset())
								.withKeepAlive(true).withSecure(false))
				.respond(
						response()
								.withStatusCode(200)
								.withHeader(new Header("Content-Type","application/*+json"))
								.withBody(mapper.writeValueAsString(response))
						.withDelay(TimeUnit.SECONDS, 1)
				);

		MFSResponse<TransactionResponse> transactionResponse = integrationService.submitTransaction(transactionLog);
		assertThat(transactionResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
	}

	/**
	 * Failure test for calling the client end-point URL with a missing config value:
	 * SUBMIT_TRANSACTION_URL
	 */
	@Test
	public void test_for_calling_client_with_missing_config_value(){
		when(configService.getConfigByKey(SUBMIT_TRANSACTION_URL))
				.thenReturn(null);
		MFSResponse<TransactionResponse> response = integrationService.submitTransaction(transactionLog());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	/**
	 * Success test for calling the client transaction status end-point URL
	 *
	 * @throws Exception
	 */
	@Test
	public void test_query_transaction() throws Exception{
		TransactionResponse response = TransactionResponse.builder()
				.mfsReferenceId("MFS20210902001")
				.transactionId("BANK20210902001")
				.amount(200.00D)
				.primaryAccountNumber("+2547110000")
				.responseCode(0)
				.responseDesc("CREATED")
				.build();

		new MockServerClient("localhost", 8080)
				.when(
						HttpRequest.request("/mpambaservice/v1/CheckB2CTransaction")
								.withMethod("GET")
								.withKeepAlive(true).withSecure(false))
				.respond(
						response()
								.withStatusCode(200)
								.withHeader(new Header("Content-Type","application/*+json"))
								.withBody(mapper.writeValueAsString(response))
								.withDelay(TimeUnit.SECONDS, 1)
				);

		MFSResponse<TransactionResponse> transactionResponse = integrationService.queryTransaction(transactionLog());
		assertThat(transactionResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
	}

	/**
	 * Failure test for calling the client end-point URL with a missing config value:
	 * QUERY_SERVICE_URL
	 */
	@Test
	public void test_for_calling_client_with_missing_query_url(){
		when(configService.getConfigByKey(QUERY_SERVICE_URL))
				.thenReturn(null);
		MFSResponse<TransactionResponse> response = integrationService.queryTransaction(transactionLog());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	/**
	 * Mock transaction log object used for testing purposes
	 *
	 * @return complete TransactionLog object
	 */
	private TransactionLog transactionLog(){
		Random genId = new Random();
		return TransactionLog.builder()
				.transactionId("TRANS"+genId.nextInt(100))
				.mfsReferenceId("MFSTest"+genId.nextInt(20000))
				.primaryAccountNumber("+278646523589")
				.amount(100D)
				.dateLogged(new Date())
				.build();
	}

	/**
	 * Mock System config table used for testing purposes
	 *
	 * @param key   is the key of the value to be saved and returned
	 * @param value is the value of the key that is saved
	 * @return System config
	 */
	public static Optional<SystemConfig> systemConfig(String key, String value) {
		return Optional.of(SystemConfig.builder()
				.configKey(key)
				.configValue(value)
				.build());
	}

}