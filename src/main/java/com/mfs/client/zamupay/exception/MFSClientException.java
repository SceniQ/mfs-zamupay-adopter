package com.mfs.client.zamupay.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Exception for all generic exception occurred in Adopter
 *
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class MFSClientException extends RuntimeException {

	public MFSClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public MFSClientException(String message) {
		super(message);
	}
}
