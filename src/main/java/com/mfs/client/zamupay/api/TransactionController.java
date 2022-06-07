package com.mfs.client.zamupay.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfs.client.zamupay.api.dto.MFSResponse;
import com.mfs.client.zamupay.api.dto.TransactionRequest;
import com.mfs.client.zamupay.api.dto.TransactionResponse;
import com.mfs.client.zamupay.api.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Transaction controller API for handling all Remittance transaction operations
 * <p>
 * This includes:
 * <ul>
 * <li>Creates Remittance request for client</li>
 * <li>Querying status of Remittance request made by client</li>
 * </ul>
 */

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bank", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    final TransactionService transactionService;
    final ObjectMapper mapper;

    /**
     * End-point for creating remittance transaction request for client
     *
     * @param transactionRequest which represent a transaction request
     * @return transaction response
     */
    @ApiOperation("Creates remittance transaction request for client.")
    @PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitTransaction(@RequestBody TransactionRequest transactionRequest) throws Exception {
        log.info("Request: {}", mapper.writeValueAsString(transactionRequest));
        MFSResponse<TransactionResponse> mfsResponse = transactionService.submitTransaction(transactionRequest);
        return new ResponseEntity<>(mfsResponse.getResponse(), HttpStatus.valueOf(mfsResponse.getStatusCode()));
    }

    /**
     * End-point for querying status of remittance transaction request created by client
     *
     * @param mfsReferenceId MFS reference of remittance transaction request
     * @return status of remittance transaction request made by client
     */
    @ApiOperation("Fetches status of remittance transaction request made by client.")
    @GetMapping(value = "/getStatus/{mfsReferenceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> queryTransaction(@PathVariable String mfsReferenceId)throws Exception {
        log.info("Transaction id: {}", mfsReferenceId);
        MFSResponse<TransactionResponse> mfsResponse = transactionService.queryTransaction(mfsReferenceId);
        return new ResponseEntity<>(mfsResponse.getResponse(), HttpStatus.valueOf(mfsResponse.getStatusCode()));
    }

}
