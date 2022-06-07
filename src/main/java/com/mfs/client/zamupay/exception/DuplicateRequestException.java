package com.mfs.client.zamupay.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This is the Duplicate Request exception thrown when there is a duplicate request being sent
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class DuplicateRequestException extends RuntimeException{

    public DuplicateRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateRequestException(String message) {
        super(message);
    }
}
