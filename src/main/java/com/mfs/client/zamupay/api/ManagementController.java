package com.mfs.client.zamupay.api;

import com.mfs.client.zamupay.api.service.ConfigService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for refreshing System configuration.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/bank/")
public class ManagementController {

    final ConfigService configService;

    /**
     * End-point used to show/retrieve all the system configuration values
     *
     * @return Map object with a list of system configuration values
     */
    @ApiOperation("Retrieves all system configuration defined for application.")
    @GetMapping(value = "/mgmt/showConfigs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> listConfig() {
        return configService.getConfig();
    }

    /**
     * End-point for adding new system configuration values
     *
     * @param key         is used to offer the name of the value to be entered
     * @param configValue is the actual data that is inserted alongside the key
     * @return Map object with a list of all the system configuration values
     * including the newly added
     */
    @ApiOperation("Adds a new system configuration.")
    @PostMapping(value = "/mgmt/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> addConfig(@RequestParam String key, @RequestParam String configValue) {
        configService.addConfig(key, configValue);
        return configService.getConfig();
    }

    /**
     * End-point for updating the system configuration
     *
     * @param key         used to identify the stored key and value pair
     * @param configValue the actual value of the key that is going to be changed
     * @return Map object with a list of the system configuration values including
     * the newly updated
     */
    @ApiOperation("Updates value for a existing system configuration for given key.")
    @PutMapping(value = "/mgmt/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> updateConfig(@PathVariable String key, @RequestParam String configValue) {
        configService.updateConfig(key, configValue);
        return configService.getConfig();
    }

    /**
     * End-point used for deleting an existing system configuration value
     *
     * @param key is used for finding the existing system configuration value that
     *            will be deleted
     * @return Map object with a list of the system configuration values excluding
     * the recently deleted
     */
    @ApiOperation("Deletes a existing system configuration for given key.")
    @DeleteMapping(value = "/mgmt/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> deleteConfig(@PathVariable String key) {
        configService.deleteConfig(key);
        return configService.getConfig();
    }
}
