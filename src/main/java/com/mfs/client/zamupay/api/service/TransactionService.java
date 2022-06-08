package com.mfs.client.zamupay.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfs.client.zamupay.api.dto.MFSResponse;
import com.mfs.client.zamupay.api.dto.TransactionRequest;
import com.mfs.client.zamupay.api.dto.TransactionResponse;
import com.mfs.client.zamupay.api.integration.ClientIntegrationService;
import com.mfs.client.zamupay.exception.DuplicateRequestException;
import com.mfs.client.zamupay.exception.MCCMNCNotFoundException;
import com.mfs.client.zamupay.exception.NoCountryFoundException;
import com.mfs.client.zamupay.exception.NoSuchTransactionExistsException;
import com.mfs.client.zamupay.infrastucture.ResponseCode;
import com.mfs.client.zamupay.persistence.MCCMNCMasterRepository;
import com.mfs.client.zamupay.persistence.TransactionRepository;
import com.mfs.client.zamupay.persistence.model.AccessToken;
import com.mfs.client.zamupay.persistence.model.MCCMNCMaster;
import com.mfs.client.zamupay.persistence.model.TransactionLog;
import com.mfs.client.zamupay.persistence.model.TransactionRoute;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static com.mfs.client.zamupay.infrastucture.MFSConstants.OPERATOR_NAME;
import static java.util.Objects.isNull;

/**
 * Class implements service for creating remittance (money transfer) request for client and
 * querying transaction status.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class TransactionService {

    final TransactionRepository transactionRepository;
    final ClientIntegrationService integrationService;
    final ConfigService configService;
    final MCCMNCMasterRepository mccmncRepository;
    final ObjectMapper mapper;

    /**
     * Checks for connectivity
     *
     * @return accessToken object
     */
    public MFSResponse<AccessToken> getAccessToken() {
        AccessToken accessToken = integrationService.getAccessToken();
        return MFSResponse.<AccessToken>builder()
                .statusCode(HttpStatus.OK.value())
                .response(accessToken)
                .build();
    }

    /**
     * Creates remittance (money transfer) request and calls client end-point
     *
     * @param request represents details of money transfer request
     * @return response for money transfer request
     */
    public MFSResponse<TransactionResponse> submitTransaction(final TransactionRequest request)throws Exception {
        //Validate request
        validateRequest(request);

        //Checking for a duplicate transaction
        isDuplicateTransaction(request.getMfsReferenceId());

        //Create log in transactionLog
        TransactionLog transactionLog = createTransactionLog(request);

        //Call payment routes end point
        //map payment route to transaction

        //Calling the client end point for money transfer request.
        MFSResponse<TransactionResponse> response = integrationService.submitTransaction(transactionLog);

        log.info("Response: {}", mapper.writeValueAsString(response));
        return response;
    }

    /**
     * Checks status of remittance (money transfer) request made by client by calling client bank end-point
     *
     * @param mfsReferenceId represents mfsReferenceId to be used in querying the transaction status
     */
    public MFSResponse<TransactionResponse> queryTransaction(final String mfsReferenceId)throws Exception {
        //checking if transaction is present
        Optional<TransactionLog> transactionEntity = transactionRepository.findByMfsReferenceId(mfsReferenceId);
        if (!transactionEntity.isPresent()) {
            throw new NoSuchTransactionExistsException("Transaction Id not found" + mfsReferenceId);
        }
        TransactionLog transactionLog = transactionEntity.get();

        //Calling the client end point for query transaction status
        MFSResponse<TransactionResponse> response = integrationService.queryTransaction(transactionLog);

        log.info("Query transaction response: {}", mapper.writeValueAsString(response));
        return response;

    }


    /**
     * Records client money transfer request in transaction log table.
     *
     * @param request this represents the transaction request which contains
     *                attributes needed to create the transaction log
     * @return transactionLog database record
     */
    private TransactionLog createTransactionLog(final TransactionRequest request) {
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setMfsReferenceId(request.getMfsReferenceId());
        transactionLog.setRecipientMCCMNC(prepareMCCMNC(request.getRecipient().getPhoneNumber()));
        transactionLog.setDateLogged(new Date());

        // set other required fields (transaction detail)

        return transactionRepository.save(transactionLog);
    }

    /**
     * Maps the transaction route ID and route channel type to the transaction log for request preparation
     * @param transactionLog is the object which will be mapped with the values
     * @param route is object containing the route details to be mapped
     */
    private void mapPaymentRouteToTransaction(TransactionLog transactionLog, TransactionRoute route) {
        transactionLog.setTransactionRouteId(route.getRouteId());
        transactionLog.setTransactionChannelType(route.getChannelType());
    }

    /**
     * This method prepares the MCCMNC from the phoneNumber and operatorCode provided in the
     *
     * @param phoneNumber this text represents the phone number which the MCCMNC will be acquired from
     * @return String
     */
    private String prepareMCCMNC(String phoneNumber) {

        //Get country code
        String countryCode = configService.getCountryByPhoneNumber(phoneNumber);

        //Throw exception if country code is not found
        if (isNull(countryCode))
            throw new NoCountryFoundException("Country code for ".concat(phoneNumber).concat(" could not be found."));

        //Check for MNC and MCC from MCCMNC master table
        Optional<MCCMNCMaster> mncmccMasterOptional = mccmncRepository.findByCountryCodeAndOperatorName(countryCode, OPERATOR_NAME);

        //Throw exception if MNC and MCC is not found in the table
        if (!mncmccMasterOptional.isPresent())
            throw new MCCMNCNotFoundException("MNC and MCC for  country code: ".concat(countryCode)
                    .concat(" and operator code: ").concat(OPERATOR_NAME).concat(" could not be find."));

        //Get record from the table
        MCCMNCMaster mccmncMaster = mncmccMasterOptional.get();
        //Return MCC and mnc as one string
        return mccmncMaster.getMcc().concat(mccmncMaster.getMnc());
    }

        /**
         * Checks whether money transfer request already exists for MFS reference id.
         *
         * @param mfsReferenceId represents MFS reference
         */
    private void isDuplicateTransaction(final String mfsReferenceId) {
        Optional<TransactionLog> transactionLog = transactionRepository.findByMfsReferenceId(mfsReferenceId);
        if (transactionLog.isPresent()) {
            throw new DuplicateRequestException(ResponseCode.DUPLICATE_TRANSACTION.getDescription());
        }
    }

    /**
     * Validates mandatory fields for money transfer request
     *
     * @param request represents the transaction request which has attributes
     *                needed by the transactionRequest
     */
    private void validateRequest(final TransactionRequest request) {
        if (isNull(request)) {
            throw new RuntimeException("Transaction request cannot be null");
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<TransactionRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Missing required field.", violations);
        }

//        if (request.getAmount() <= 0) {
//            throw new ConstraintViolationException("Amount invalid.", null);
//        }
    }
}
