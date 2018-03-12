package com.mms.cloud.search.response.aggregations.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mms.cloud.search.response.aggregations.Aggregation;

import java.util.List;

/**
 * Histogram aggregation result.
 */
public class HistogramAggregation extends Aggregation {
    @JsonProperty("buckets")
    private List<HistogramBucket> buckets;

    public List<HistogramBucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<HistogramBucket> buckets) {
        this.buckets = buckets;
    }
}
