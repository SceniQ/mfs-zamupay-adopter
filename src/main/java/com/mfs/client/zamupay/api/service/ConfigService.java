package com.mfs.client.zamupay.api.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mfs.client.zamupay.exception.MissingConfigValueException;
import com.mfs.client.zamupay.persistence.CountryMasterRepository;
import com.mfs.client.zamupay.persistence.IdentityTypeRepository;
import com.mfs.client.zamupay.persistence.SystemConfigRepository;
import com.mfs.client.zamupay.persistence.model.CountryMaster;
import com.mfs.client.zamupay.persistence.model.IdentityType;
import com.mfs.client.zamupay.persistence.model.SystemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Configuration service used to acquire all the configuration information
 * needed within the application
 */
@Service
@RequiredArgsConstructor
public class ConfigService {

	final CountryMasterRepository countryRepository;
	final SystemConfigRepository configRepository;
	final IdentityTypeRepository identityTypeRepository;


	/**
	 * This method returns the country from the phoneNumber text
	 *
	 * @param phoneNumberText represents the phone number to used for acquiring the country code
	 * @return String which consists of the country code
	 */
	public String getCountryByPhoneNumber(String phoneNumberText) {

		if (StringUtils.isEmpty(phoneNumberText)) {
			return null;
		}

		try {
			PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
			phoneNumberText = phoneNumberText.trim();
			if (!phoneNumberText.startsWith("+")) {
				phoneNumberText = "+".concat(phoneNumberText);
			}
			Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberText, "");

			CountryMaster countryMaster = countryRepository.findByPhoneCode(phoneNumber.getCountryCode());
			return countryMaster.getCountryCode();
		} catch (NumberParseException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	/**
	 * This method returns the numeric country code from the phoneNumber text
	 *
	 * @param phoneNumberText represents the phone number to used for acquiring the numeric country code
	 * @return String which consists of the country code
	 */
	public String getNumericCountryCodeByPhoneNumber(String phoneNumberText) {
		String countryCode = this.getCountryByPhoneNumber(phoneNumberText);
		CountryMaster master = countryRepository.findByCountryCode(countryCode)
				.orElseThrow(() -> new MissingConfigValueException("No country code exists for: " + countryCode));
		return master.getNumericCode();
	}

		/**
         * Retrieves identity type from the DB
         *
         * @param value represents the type of identity that should be retrieved
         * @return an identity type
         */
	public String getIdentityType(String value) {
		IdentityType identity = identityTypeRepository.findByIdValue(value)
				.orElseThrow(() -> new MissingConfigValueException("No identity type exists for: " + value));
		return identity.getIdType();
	}

	/**
	 * Retrieve system configuration
	 *
	 * @return a map of configuration values
	 */
	public Map<String, String> getConfig() {
		final Map<String, String> systemConfiguration = new HashMap<>();
		List<SystemConfig> configurations = configRepository.findAll();
		configurations.forEach(config -> systemConfiguration.put(config.getConfigKey(), config.getConfigValue()));
		return systemConfiguration;
	}

	/**
	 * Retrieves configuration value by key
	 *
	 * @param key represents the text which will be used to retrieve the
	 *            configuration value
	 * @return configuration value for key
	 */
	public String getConfigByKey(final String key) {
		Optional<SystemConfig> config = configRepository.findByConfigKey(key);
		if (!config.isPresent())
			throw new MissingConfigValueException("No such value defined in System configuration for key:" + key);
		return config.get().getConfigValue();
	}

	/**
	 * Retrieves Boolean configuration value by key
	 *
	 * @param key represent the text which will be used to retrieve the
	 *            configuration value
	 * @return Boolean configuration value for key
	 */
	public Boolean getBooleanConfigByKey(String key) {
		Optional<SystemConfig> config = configRepository.findByConfigKey(key);
		if (!config.isPresent())
			throw new MissingConfigValueException("No such value defined in System configuration for key:" + key);

		return Boolean.valueOf(config.get().getConfigValue());
	}

	/**
	 * Retrieves Integer configuration value by key
	 *
	 * @param key represents the text which will be used to retrieve the
	 *            configuration value
	 * @return Integer configuration value for key
	 */
	public Integer getIntConfigByKey(final String key) {
		Optional<SystemConfig> config = configRepository.findByConfigKey(key);
		if (!config.isPresent())
			throw new MissingConfigValueException("No such value defined in System configuration for key:" + key);

		try {
			return Integer.parseInt(config.get().getConfigValue());
		} catch (NumberFormatException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Adds a new system configuration key and value
	 *
	 * @param key   text used to identify the value saved in the configuration table
	 * @param value text is the actual value which is to be added alongside the key
	 */
	public void addConfig(final String key, final String value) {
		Optional<SystemConfig> config = configRepository.findByConfigKey(key);
		if (config.isPresent())
			throw new RuntimeException("System configuration already contains value for key:" + key);

		configRepository.save(SystemConfig.builder().configKey(key).configValue(value).build());

	}

	/**
	 * Updates the system configuration for given key
	 *
	 * @param key   is the text used to find the system configuration value in the
	 *              table
	 * @param value is the text which carries the data that is used to update the
	 *              configuration value
	 */
	public void updateConfig(String key, String value) {
		Optional<SystemConfig> optional = configRepository.findByConfigKey(key);
		if (!optional.isPresent())
			throw new MissingConfigValueException("No such value defined in System configuration for key:" + key);

		SystemConfig config = optional.get();
		config.setConfigValue(value);
		configRepository.save(config);
	}

	/**
	 * Deletes an existing configuration value in the database
	 *
	 * @param key is the text used to find the existing system configuration key and
	 *            value
	 */
	public void deleteConfig(String key) {
		Optional<SystemConfig> optional = configRepository.findByConfigKey(key);
		if (!optional.isPresent())
			throw new MissingConfigValueException("No such value defined in System configuration for key:" + key);

		SystemConfig config = optional.get();
		configRepository.delete(config);
	}

}
