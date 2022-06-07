package com.mfs.client.zamupay.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;

import java.util.Optional;

import com.mfs.client.zamupay.persistence.model.SystemConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests the system configuration repository
 */

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-junit.properties")
public class SystemConfigRepositoryTest {

	@Autowired
	private SystemConfigRepository systemConfigRepository;

	/**
	 * Test for saving the complete SystemConfig Object
	 **/
	@Test
	public void happy_test_for_system_config() {
		SystemConfig savedSystemConfig = systemConfigRepository.save(systemConfig());
		assertThat(savedSystemConfig).isNotNull();
		assertThat(savedSystemConfig.getConfigKey()).isEqualToIgnoringCase("TEST_KEY");
		assertThat(savedSystemConfig.getConfigValue()).isSameAs("TEST_VALUE");
	}

	/**
	 * System configuration happy test case for retrieving an existing system
	 * configuration by configuration key.
	 **/
	@Test
	public void test_to_retrieve_system_config_by_config_key() {
		SystemConfig config = systemConfigRepository.save(systemConfig());
		Optional<SystemConfig> fetchedConfig = systemConfigRepository.findByConfigKey("TEST_KEY");
		assertEquals(config, fetchedConfig.get());
	}

	/**
	 * System configuration failure test case for retrieving a system configuration
	 * by non existing configuration key.
	 **/
	@Test
	public void test_to_retrieve_null_system_config_by_invalid_config_key() {
		systemConfigRepository.save(systemConfig());
		Optional<SystemConfig> retrieveSysConfig = systemConfigRepository.findByConfigKey("Key");
		assertEquals(retrieveSysConfig, Optional.empty());
	}

	/**
	 * System configuration test returns null for all missing fields.
	 **/
	@Test(expected = DataAccessException.class)
	public void test_for_null_system_config_key() {

		// no configuration key
		systemConfigRepository.save(SystemConfig.builder().configValue("TEST_VALUE").build());
		fail("This line should never execute");

	}

	/**
	 * System configuration test returns null for all missing fields.
	 **/
	@Test(expected = DataAccessException.class)
	public void test_for_null_system_config_value() {

		// no configuration value
		systemConfigRepository.save(SystemConfig.builder().configKey("TEST_KEY").build());
		fail("This line should never execute");

	}

	/**
	 * System configuration test with complete request
	 *
	 * @return systemConfig object
	 */
	private SystemConfig systemConfig() {
		return SystemConfig.builder().configKey("TEST_KEY").configValue("TEST_VALUE").build();
	}

}
