package com.mfs.client.zamupay.persistence;

import com.mfs.client.zamupay.persistence.model.MCCMNCMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This the MCCMNC repository responsible for retrieving data relating to the
 * MCCMNC Master used within sending a request to third party
 */
public interface MCCMNCMasterRepository extends JpaRepository<MCCMNCMaster, Integer> {
    /**
     * This method retrieves a MCCMNC master object using the Country Code and Operator Name.
     *
     * @param countryCode it is the text used to find the MCCMNC master.
     * @param operatorName it is the text used to find the MCCMNC master.
     * @return MCCMNC master object.
     */
    Optional<MCCMNCMaster> findByCountryCodeAndOperatorName(String countryCode, String operatorName);
}
