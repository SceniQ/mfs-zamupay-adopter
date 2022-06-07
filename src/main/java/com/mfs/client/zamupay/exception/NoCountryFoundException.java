package com.mfs.client.zamupay.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This is no country found exception thrown when when country code not found
 * for request
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class NoCountryFoundException extends RuntimeException {

	public NoCountryFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoCountryFoundException(String message) {
		super(message);
	}
}
