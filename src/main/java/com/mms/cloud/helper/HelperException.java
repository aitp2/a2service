package com.mms.cloud.helper;

/**
 * Exception thrown when something goes wrong in the helper classes.
 */
public class HelperException extends RuntimeException {
    public HelperException(String message) {
        super(message);
    }
}
