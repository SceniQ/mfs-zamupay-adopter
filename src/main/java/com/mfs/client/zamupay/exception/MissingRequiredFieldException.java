package com.mfs.client.zamupay.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This is the Missing required field exception thrown when a request is made with missing
 * mandatory fields
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class MissingRequiredFieldException extends RuntimeException{

    public MissingRequiredFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingRequiredFieldException(String message) {
        super(message);
    }
}
