package com.mms.cloud.search.response.aggregations.metric;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mms.cloud.document.response.Shards;

public class MetricResponse {
    @JsonProperty(value = "_shards")
    private Shards shards;

    private long count;

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
