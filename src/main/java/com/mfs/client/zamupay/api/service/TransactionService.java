package com.mfs.client.zamupay.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfs.client.zamupay.api.dto.MFSResponse;
import com.mfs.client.zamupay.api.dto.TransactionRequest;
import com.mfs.client.zamupay.api.dto.TransactionResponse;
import com.mfs.client.zamupay.api.integration.ClientIntegrationService;
import com.mfs.client.zamupay.exception.DuplicateRequestException;
import com.mfs.client.zamupay.exception.NoSuchTransactionExistsException;
import com.mfs.client.zamupay.infrastucture.ResponseCode;
import com.mfs.client.zamupay.persistence.TransactionRepository;
import com.mfs.client.zamupay.persistence.model.AccessToken;
import com.mfs.client.zamupay.persistence.model.TransactionLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

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
    final ObjectMapper mapper;

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
     * Records client money transfer request in transaction log table.
     *
     * @param request this represents the transaction request which contains
     *                attributes needed to create the transaction log
     * @return transactionLog database record
     */
    private TransactionLog createTransactionLog(final TransactionRequest request) {
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setMfsReferenceId(request.getMfsReferenceId());
        transactionLog.setReceiverMsisdn(request.getPrimaryAccountNumber());
        transactionLog.setAmount(request.getAmount());
        transactionLog.setPrimaryAccountNumber(request.getPrimaryAccountNumber());
        transactionLog.setDateLogged(new Date());

        // set other required fields

        return transactionRepository.save(transactionLog);
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

        if (request.getAmount() <= 0) {
            throw new ConstraintViolationException("Amount invalid.", null);
        }
    }
}
