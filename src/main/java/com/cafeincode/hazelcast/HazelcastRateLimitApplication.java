package com.cafeincode.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HazelcastRateLimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastRateLimitApplication.class, args);
    }

}
