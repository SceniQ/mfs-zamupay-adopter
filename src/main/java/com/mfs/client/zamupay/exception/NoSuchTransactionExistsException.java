package com.mfs.client.zamupay.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * No transaction exists exception thrown when a client queries transaction with
 * an id that does not exist
 **/
@ToString
@EqualsAndHashCode(callSuper = true)
public class NoSuchTransactionExistsException extends RuntimeException {

	public NoSuchTransactionExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchTransactionExistsException(String message) {
		super(message);
	}
}
