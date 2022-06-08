package com.mfs.client.zamupay.persistence;

import com.mfs.client.zamupay.persistence.model.TransactionRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for storing and retrieving data into the transaction route table
 */
public interface TransactionRouteRepository extends JpaRepository<TransactionRoute,Long> {
    /**
     * Retrieves the transaction route details
     * @param channelType used to find an existing transaction route
     * @return transaction route record
     */
    Optional<TransactionRoute> findByChannelType(int channelType);
}
