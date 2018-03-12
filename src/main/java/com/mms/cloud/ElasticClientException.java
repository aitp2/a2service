package com.mms.cloud;

/**
 * Parent exception for all custom com.mms.cloud based exceptions while using the client.
 */
public class ElasticClientException extends RuntimeException {
    public ElasticClientException(String message) {
        super(message);
    }

    public ElasticClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
