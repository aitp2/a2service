package com.mms.cloud.search.response.aggregations.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mms.cloud.search.response.aggregations.Aggregation;

import java.util.List;

/**
 * Date Histogram representation of an aggregation.
 */
public class DateHistogramAggregation extends Aggregation {
    @JsonProperty("buckets")
    private List<DateHistogramBucket> buckets;

    public List<DateHistogramBucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<DateHistogramBucket> buckets) {
        this.buckets = buckets;
    }
}
