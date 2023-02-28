package com.cafeincode.hazelcast.model;

import lombok.Data;

@Data
public class ConfigRateLimit {

    private long limit;
    private long second;
}
