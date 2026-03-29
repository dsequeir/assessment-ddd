package com.example.assessmentddd;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class HazelcastConfiguration {

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setClusterName("prices-cache-cluster");

        config.addMapConfig(
                new MapConfig()
                        .setName("prices-cache")
                        .setTimeToLiveSeconds(300)
                        .setEvictionConfig(
                                new EvictionConfig()
                                        .setEvictionPolicy(EvictionPolicy.LRU)
                                        .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                                        .setSize(1000)
                        )
        );

        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config config) {
        return Hazelcast.newHazelcastInstance(config);
    }
}