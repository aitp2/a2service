package com.mms.cloud.cluster;

import com.mms.cloud.ElasticClientException;

/**
 * Exception thrown while interacting with the cluster api.
 */
public class ClusterApiException extends ElasticClientException {
    public ClusterApiException(String message) {
        super(message);
    }

    public ClusterApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
