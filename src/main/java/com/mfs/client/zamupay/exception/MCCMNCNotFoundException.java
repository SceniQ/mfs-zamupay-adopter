package com.mfs.client.zamupay.exception;

public class MCCMNCNotFoundException extends RuntimeException{
    public MCCMNCNotFoundException(String message) {
        super(message);
    }

    public MCCMNCNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
