package com.mfs.client.zamupay.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This is the missing configuration value exception thrown when
 * the required configuration value is non existent
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class MissingConfigValueException extends RuntimeException {

	public MissingConfigValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingConfigValueException(String message) {
		super(message);
	}

}