package com.cafeincode.hazelcast.config;

import com.cafeincode.hazelcast.model.ConfigRateLimit;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hungtv27
 */
@Configuration
public class HazelcastConfig {

    @Value("#{'${hazelcast.cafeincode.address}'.split(',')}")
    protected String[] address;

    @Value("${hazelcast.cafeincode.cluster_name}")
    private String clusterName;

    @Bean(name = "hazelcastClient")
    public HazelcastInstance hazelcastInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        clientConfig.getNetworkConfig().addAddress(address);
        clientConfig.getNetworkConfig().setConnectionTimeout(50000);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Bean(name = "configRateLimit")
    public ConfigRateLimit configRateLimit() {
        ConfigRateLimit configDefault = new ConfigRateLimit();
        configDefault.setLimit(5); // limit request 5
        configDefault.setSecond(60); //second
        return configDefault;
    }
}
