package com.mfs.client.zamupay.api.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import com.mfs.client.zamupay.api.dto.TransactionRequest;
import com.mfs.client.zamupay.exception.DuplicateRequestException;
import com.mfs.client.zamupay.exception.NoSuchTransactionExistsException;
import com.mfs.client.zamupay.infrastucture.MFSConstants;
import com.mfs.client.zamupay.persistence.SystemConfigRepository;
import com.mfs.client.zamupay.persistence.TransactionRepository;
import com.mfs.client.zamupay.persistence.model.SystemConfig;
import com.mfs.client.zamupay.persistence.model.TransactionLog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for <code>TransactionService</code>.
 * For validating all possible failure and success scenario for :
 * <ul>
 * <li>Submit transaction</li>
 * <li>Query transaction</li>
 * </ul>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-junit.properties")
@Transactional()
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;
    
    @MockBean
    SystemConfigRepository configRepository;

    @MockBean
    TransactionRepository transactionRepository;
    
	@Before
	public void setUp() {

		when(configRepository.findByConfigKey(MFSConstants.SUBMIT_TRANSACTION_URL))
				.thenReturn(Optional.of(systemConfig(MFSConstants.SUBMIT_TRANSACTION_URL, "http://localhost:8080/")));
		
		when(configRepository.findByConfigKey(MFSConstants.QUERY_SERVICE_URL))
		.thenReturn(Optional.of(systemConfig(MFSConstants.QUERY_SERVICE_URL, "http://localhost:8080/getStatus/{0}")));
	}

	/**
     * Success test for submitting transaction
     */
    @Test
    public void test_for_submit_transaction() throws Exception{
        when(transactionRepository.findByMfsReferenceId(anyString())).thenReturn(Optional.empty());
        when(transactionRepository.save(any(TransactionLog.class))).thenReturn(transactionLog());
        transactionService.submitTransaction(request());
    }

    /**
     * Failure test for submitting an invalid transaction: No primary account number
     */
    @Test(expected = ConstraintViolationException.class)
    public void test_for_invalid_request_no_primary_acc_num() throws Exception{

        TransactionRequest requestNoPrimaryAccNum = TransactionRequest.builder()
                .mfsReferenceId("MFSTest202108")
                .amount(100D)
                .build();
        transactionService.submitTransaction(requestNoPrimaryAccNum);
    }

    /**
     * Failure test for submitting an invalid transaction: No MFS ref ID
     */
    @Test(expected = ConstraintViolationException.class)
    public void test_for_invalid_request_no_mfs_ref_id() throws Exception{

        TransactionRequest requestNoMfsRefId =  TransactionRequest.builder()
                .primaryAccountNumber("+276354254361")
                .amount(100D)
                .build();
        transactionService.submitTransaction(requestNoMfsRefId);
    }

    /**
     * Failure test for submitting an invalid transaction: No amount
     */
    @Test(expected = ConstraintViolationException.class)
    public void test_for_invalid_request_no_amount() throws Exception{

       TransactionRequest requestNoAmount =  TransactionRequest.builder()
                .mfsReferenceId("MFSTest202108")
                .primaryAccountNumber("+276354254361")
                .build();
        transactionService.submitTransaction(requestNoAmount);
    }

    /**
     * Failure test for submitting an invalid transaction: amount is below zero
     */
    @Test(expected = ConstraintViolationException.class)
    public void test_for_invalid_request_low_amount()throws Exception {
        TransactionRequest request = request();
        request.setAmount(-1.0);
        transactionService.submitTransaction(request);
    }

    /**
     * Failure test for submitting duplicate request
     */
    @Test(expected = DuplicateRequestException.class)
    public void test_for_duplicate_transaction_request() throws Exception{
        when(transactionRepository.findByMfsReferenceId(any(String.class)))
                .thenReturn(Optional.of(transactionLog()));
        transactionService.submitTransaction(request());
    }

    /**
     * Success test for querying a transaction status with an existing transaction log
     */
    @Test
    public void test_for_QueryTransaction() throws Exception{
        when(transactionRepository.findByMfsReferenceId(any(String.class)))
                .thenReturn(Optional.of(transactionLog()));
        transactionService.queryTransaction("MFSTest2021");
    }

    /**
     * Failure test for querying a transaction without an existing transaction log
     */
    @Test(expected = NoSuchTransactionExistsException.class)
    public void test_for_non_existing_transaction_log_query() throws Exception{
        when(transactionRepository.findByMfsReferenceId(any(String.class)))
                .thenReturn(Optional.empty());
        transactionService.queryTransaction("MFSTest");
    }

    /**
     * Dummy transaction log object used for testing purposes
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
     * Dummy Transaction request used for testing purposes
     *
     * @return TransactionRequest object
     */
    private TransactionRequest request(){
        return TransactionRequest.builder()
                .mfsReferenceId("MFSTest202108")
                .primaryAccountNumber("+276354254361")
                .amount(100D)
                .build();

    }
    
    private SystemConfig systemConfig(String key, String value) {
    	Random genId = new Random();
    	return SystemConfig.builder().configId(genId.nextInt(100)).configKey(key).configValue(value).build();
    }



}