package com.mfs.client.zamupay.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This is the Invalid Token exception thrown when there is an invalid token being sent
 * in the request header
 */
@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
public class InvalidTokenException extends RuntimeException {

    int statusCode;

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public InvalidTokenException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
