package com.mfs.client.zamupay.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mfs.client.zamupay.exception.MissingConfigValueException;
import com.mfs.client.zamupay.persistence.SystemConfigRepository;
import com.mfs.client.zamupay.persistence.model.SystemConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests the configService implementation
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-junit.properties")
public class ConfigServiceTest {


	@MockBean
	SystemConfigRepository systemConfigRepository;

	@Autowired
	private ConfigService configService;

	/**
	 * Success test to update system configuration values
	 **/
	@Test
	public void update_system_Config_values(){

		SystemConfig systemConfig =  completeSystemConfig().get(0);

		when(systemConfigRepository.findByConfigKey(any(String.class))).thenReturn(Optional.of(completeSystemConfig().get(0)));
		when(systemConfigRepository.save(any(SystemConfig.class))).thenReturn(completeSystemConfig().get(0));
		configService.updateConfig(systemConfig.getConfigKey(),systemConfig.getConfigValue());

	}

	/**
	 * Success test to retrieve the configuration values
	 */

	@Test
	public void retrieve_config_values(){

		Map<String, String> testConfigValues = configService.getConfig();
		assertThat(testConfigValues).isNotNull();
	}

	/**
	 * Failure test when there is no values in system configuration
	 */
	@Test(expected = MissingConfigValueException.class)
	public void get_config_by_key() {
		when(systemConfigRepository.findByConfigKey(any(String.class))).thenReturn(Optional.empty());
		configService.getConfigByKey("MOCK_CONFIG");

	}

	/**
	 * Success test for retrieving a boolean config value
	 */
	@Test
	public void test_for_retrieving_a_boolean_config_value(){
		when(systemConfigRepository.findByConfigKey(any(String.class)))
				.thenReturn(Optional.of(completeSystemConfig().get(1)));

		boolean value = configService.getBooleanConfigByKey("BOOLEAN_VALUE");
		assertTrue(value);
	}

	/**
	 * Failure test for retrieving a non existing boolean config value
	 */
	@Test(expected = MissingConfigValueException.class)
	public void test_for_retrieving_a_non_existent_boolean_config_value(){
		when(systemConfigRepository.findByConfigKey("BOOLEAN_KEY"))
				.thenReturn(Optional.empty());

		configService.getBooleanConfigByKey("BOOLEAN_KEY");

	}

	/**
	 * Success test for retrieving a int config value
	 */
	@Test
	public void test_for_retrieving_an_int_config_value(){
		when(systemConfigRepository.findByConfigKey(any(String.class)))
				.thenReturn(Optional.of(completeSystemConfig().get(2)));

		int value = configService.getIntConfigByKey("INT_VALUE");
		assertThat(value).isGreaterThan(0);
	}

	/**
	 * Failure test for retrieving a wrong int config value
	 */
	@Test(expected = MissingConfigValueException.class)
	public void test_for_retrieving_a_wrong_int_config_value(){
		when(systemConfigRepository.findByConfigKey("INT_VALUE"))
				.thenReturn(Optional.empty());
		configService.getIntConfigByKey("INT_VALUE");

	}

	/**
	 * Failure test for retrieving a illegal format int config value
	 */
	@Test(expected = RuntimeException.class)
	public void test_for_retrieving_illegal_format_int_config_value(){
		when(systemConfigRepository.findByConfigKey("INT_VALUE"))
				.thenReturn(Optional.of(completeSystemConfig().get(3)));
		configService.getIntConfigByKey("INT_VALUE");

	}

	/**
	 * Failure test for deleting a non existent config value
	 */
	@Test(expected = MissingConfigValueException.class)
	public void test_for_deleting_a_non_existent_config_value(){
		when(systemConfigRepository.findByConfigKey(any(String.class)))
				.thenReturn(Optional.empty());
		configService.deleteConfig("MOCK_VALUE");

	}

	/**
	 * Success test to add new system configuration key and value
	 */
	@Test
	public void add_config()
	{
		SystemConfig config = new SystemConfig();

		config.setConfigKey("SECURITY_KEY");
		config.setConfigValue("H7A5WO/GULNf27Iu4V+WsA==");

		when(systemConfigRepository.save(any(SystemConfig.class))).thenReturn(config);
		systemConfigRepository.save(config);
	}


	/**
	 * This is a list of Mock system configuration values
	 *
	 * @return list of system config values
	 */
	private List<SystemConfig> completeSystemConfig() {

		List<SystemConfig> configs = new ArrayList<>();


		SystemConfig config = new SystemConfig();
		config.setConfigKey("SECURITY_KEY");
		config.setConfigValue("H7A5WO/GULNf27Iu4V+WsA==");

		SystemConfig config1 = new SystemConfig();
		config1.setConfigKey("BOOLEAN_VALUE");
		config1.setConfigValue("true");

		SystemConfig config2 = new SystemConfig();
		config2.setConfigKey("INT_VALUE");
		config2.setConfigValue("1");

		SystemConfig config3 = new SystemConfig();
		config3.setConfigKey("INT_VALUE2");
		config3.setConfigValue("ONE");

		configs.add(config);
		configs.add(config1);
		configs.add(config2);
		configs.add(config3);

		return configs;
	}


}