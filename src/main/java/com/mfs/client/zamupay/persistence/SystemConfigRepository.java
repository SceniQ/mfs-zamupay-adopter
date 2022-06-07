package com.mfs.client.zamupay.persistence;

import com.mfs.client.zamupay.persistence.model.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class for performing all database operations on System configuration
 */
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {

	/**
	 * This method retrieves existing system configuration objects using
	 * configuration key.
	 *
	 * @param configKey it is the text used to find the existing system configuration.
	 * @return System Configuration objects
	 */

	Optional<SystemConfig> findByConfigKey(String configKey);

}
