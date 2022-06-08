package com.mfs.client.zamupay.persistence;

import com.mfs.client.zamupay.persistence.model.IdentityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for accessing identity type information from DB
 */
public interface IdentityTypeRepository extends JpaRepository<IdentityType, Long> {

    /**
     * Retrieves identity type from the DB
     *
     * @param value represents the type of identity that should be retrieved
     * @return an identity type record
     */
    Optional<IdentityType> findByIdValue(String value);
}
