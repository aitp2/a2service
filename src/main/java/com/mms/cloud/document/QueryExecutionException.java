package com.mms.cloud.document;

/**
 * Exception thrown when com.mms.cloud throws an error.
 */
public class QueryExecutionException extends RuntimeException {
    public QueryExecutionException(String message) {
        super(message);
    }
}
