package com.mfs.client.zamupay.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.mfs.client.zamupay.persistence.model.TransactionLog;

import junit.framework.TestCase;

/**
 * Tests the transaction repository implementation
 */

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-junit.properties")
public class TransactionRepositoryTest extends TestCase {

    @Autowired
    TransactionRepository transactionRepository;

    /**
     * Success test for saving transaction log
     */
    @Test
    public void test_for_saving_transaction_log() {
        TransactionLog transactionLog = transactionRepository.save(transactionLog());
        assertThat(transactionLog).isNotNull();
        assertThat(transactionLog.getTransactionLogId()).isGreaterThan(0);
    }

    /**
     * Success test for updating transaction log
     */
    @Test
    public void test_for_updating_transaction_log(){
        TransactionLog transactionLog = transactionRepository.save(transactionLog());
        assertThat(transactionLog).isNotNull();

        transactionLog.setResultCode(1);
        transactionLog.setResultDesc("Successfully delivered");
        transactionLog = transactionRepository.save(transactionLog);
        assertThat(transactionLog.getResultDesc()).isEqualTo("Successfully delivered");

    }

    /**
     * Success test for finding an existing transaction log
     */
    @Test
    public void test_for_finding_by_MfsReferenceId(){
        TransactionLog transaction= transactionRepository.save(transactionLog());
        Optional<TransactionLog> transactionLog = transactionRepository.findByMfsReferenceId(transaction.getMfsReferenceId());
        assertTrue(transactionLog.isPresent());
    }

    /**
     * Failure test for finding a non-existing transaction log
     */
    @Test
    public void test_for_finding_non_existing_transaction_log(){
        Optional<TransactionLog> transactionLog = transactionRepository.findByMfsReferenceId("MFSTest20210011");
        assertFalse(transactionLog.isPresent());
    }

    /**
     * Failure test for saving transaction log with missing values
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void test_for_saving_transaction_log_with_missing_values(){

        //no primary account name
        TransactionLog transactionLogNoPrimaryAcc =  TransactionLog.builder()
                .transactionId("TRANS101")
                .mfsReferenceId("MFSTest202108")
                .amount(100D)
                .resultCode(0)
                .resultDesc("Success")
                .dateLogged(new Date())
                .build();
        transactionRepository.save(transactionLogNoPrimaryAcc);

        //no MFS reference id
        TransactionLog transactionLogNoMfsRefId =  TransactionLog.builder()
                .transactionId("TRANS101")
                .primaryAccountNumber("+278646523589")
                .amount(100D)
                .resultCode(0)
                .resultDesc("")
                .dateLogged(new Date())
                .build();
        transactionRepository.save(transactionLogNoMfsRefId);

        //no date logged
        TransactionLog transactionLogNoDate =  TransactionLog.builder()
                .transactionId("TRANS101")
                .mfsReferenceId("MFSTest202108")
                .primaryAccountNumber("+278646523589")
                .amount(100D)
                .resultCode(0)
                .resultDesc("Success")
                .build();
        transactionRepository.save(transactionLogNoDate);
    }

    /**
     * Mock transaction log used for testing purposes
     *
     * @return Transaction log
     */
    private TransactionLog transactionLog(){
    	Random idGen = new Random();
        return TransactionLog.builder()
                .transactionId("TRANS"+idGen.nextInt(100))
                .mfsReferenceId("MFSTest"+idGen.nextInt(20000))
                .primaryAccountNumber("+278646523589")
                .receiverMsisdn("+237677389120")
                .amount(100D)
                .resultCode(0)
                .resultDesc("Created")
                .dateLogged(new Date())
                .build();
    }

}