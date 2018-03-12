package com.mms.cloud.index.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mms.cloud.document.response.Shards;

public class RefreshResponse {
    @JsonProperty(value = "_shards")
    private Shards shards;

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }
}
