package com.mfs.client.zamupay.api;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.Random;

import com.mfs.client.zamupay.MockMvcBase;
import com.mfs.client.zamupay.persistence.SystemConfigRepository;
import com.mfs.client.zamupay.persistence.model.SystemConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Tests the implementation of <code>ManagementController</code>
 */

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-junit.properties")
public class ManagementControllerTest extends MockMvcBase {

	@MockBean
	private SystemConfigRepository configRepository;

	/**
	 * Success test for getting a map of all available system
	 * configurations in the database
	 * 
	 * @throws Exception
	 */
	@Test
	public void listConfig() throws Exception {
		mockMvc.perform(get("/bank/mgmt/showConfigs")
				.content(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk());
	}

	/**
	 * Success test for creating a new record of system configuration
	 * 
	 * @throws Exception
	 */
	@Test
	public void addConfig() throws Exception {
		Random random = new Random();
		String key = "BANK" + "KEY" + random.nextInt();
		when(configRepository.findByConfigKey(anyString())).thenReturn(Optional.empty());
		mockMvc.perform(post("/bank/mgmt/?key=" + key + "&configValue=TNMTEST_VALUE")
							.contentType(MediaType.APPLICATION_JSON)
							.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	/**
	 * Failure test for adding system configuration record with an
	 * existing configuration key
	 * 
	 * @throws Exception
	 */
	@Test
	public void add_config_with_existing_key() throws Exception {

		SystemConfig config = systemConfig();
		when(configRepository.findByConfigKey(anyString())).thenReturn(Optional.of(config));
		mockMvc.perform(post("/bank/mgmt/?key=TEST_KEY&configValue=TEST_VALUE")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isInternalServerError());
	}

	/**
	 * Success test for updating an existing system configuration value
	 */
	@Test
	public void updateConfig() throws Exception {
		SystemConfig config = systemConfig();
		when(configRepository.findByConfigKey(anyString())).thenReturn(Optional.of(config));

		mockMvc.perform(put("/bank/mgmt/{key}", "TEST_KEY")
								.param("configValue", "NEW_TEST")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	/**
	 * Failure test for updating an existing system configuration with
	 * invalid key
	 * 
	 * @throws Exception
	 */
	@Test
	public void update_config_with_invalid_key() throws Exception {
		when(configRepository.findByConfigKey(anyString())).thenReturn(Optional.empty());
		mockMvc.perform(put("/bank/mgmt/{key}", "key")
							.param("configValue", "NEW_TEST")
							.contentType(MediaType.APPLICATION_JSON)
							.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isInternalServerError());
	}

	/**
	 * Success test for deleting a configuration value
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteConfig() throws Exception {
		SystemConfig config = systemConfig();
		when(configRepository.findByConfigKey(anyString())).thenReturn(Optional.of(config));
		mockMvc.perform(delete("/bank/mgmt/{key}", "SECURITY_KEY")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	/**
	 * Success test for deleting system configuration with invalid configuration key
	 * 
	 * @throws Exception
	 */
	@Test
	public void delete_by_invalid_config_key() throws Exception {
		mockMvc.perform(delete("/bank/mgmt/{key}", "key")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}

	private SystemConfig systemConfig() {
		SystemConfig config = SystemConfig.builder().configKey("TEST_KEY").configValue("TEST_VALUE").build();
		return config;
	}
}
