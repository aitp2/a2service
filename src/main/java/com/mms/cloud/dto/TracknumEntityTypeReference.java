package com.mms.cloud.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mms.cloud.search.response.query.ElasticQueryResponse;

/**
 * Type reference required to make use of Jackson and nested entities.
 */
public class TracknumEntityTypeReference extends TypeReference<ElasticQueryResponse<TracknumEntity>> {
}
