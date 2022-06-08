package com.mfs.client.zamupay.persistence;

import com.mfs.client.zamupay.persistence.model.CountryMaster;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is the CountryMaster repository responsible for retrieving Country Master information
 */
public interface CountryMasterRepository extends JpaRepository<CountryMaster, Integer> {
    /**
     * This method retrieves a country master object using the Phone Code.
     *
     * @param countryCode it is the text used to find the country master.
     * @return Country Master object.
     */
    CountryMaster findByPhoneCode(int countryCode);
}
