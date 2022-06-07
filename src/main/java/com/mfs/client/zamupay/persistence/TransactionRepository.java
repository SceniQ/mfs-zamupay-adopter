package com.mfs.client.zamupay.persistence;

import com.mfs.client.zamupay.persistence.model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class for performing all database operations on transaction log entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<TransactionLog, Integer> {
    /**
     * This method retrieves a transaction log object using the mfsReferenceId.
     *
     * @param mfsReferenceId it is the text used to find the existing transaction log.
     * @return transaction log
     */
    Optional<TransactionLog> findByMfsReferenceId(String mfsReferenceId);
}
