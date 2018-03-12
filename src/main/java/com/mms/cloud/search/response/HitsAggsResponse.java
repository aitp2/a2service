package com.mms.cloud.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mms.cloud.search.response.aggregations.Aggregation;
import com.mms.cloud.search.response.aggregations.AggregationKeyDeserializer;

import java.util.Map;

/**
 * Main response class for aggregations response. Can contain hits and aggregations
 */
public class HitsAggsResponse<T> extends HitsResponse<T> {

    @JsonProperty
    @JsonDeserialize(keyUsing = AggregationKeyDeserializer.class)
    private Map<String, Aggregation> aggregations;

    public Map<String, Aggregation> getAggregations() {
        return aggregations;
    }

    public void setAggregations(Map<String, Aggregation> aggregations) {
        this.aggregations = aggregations;
    }
}
