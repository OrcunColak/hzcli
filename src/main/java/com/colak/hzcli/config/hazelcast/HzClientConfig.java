package com.colak.hzcli.config.hazelcast;

import com.colak.hzcli.config.Profiles;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(Profiles.DEFAULT)
@Slf4j
public class HzClientConfig {

    @Bean
    public ClientConfig hazelcastConfig() {
        return new ClientConfig();
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        log.info("Client profile is active");
        return HazelcastClient.newHazelcastClient(hazelcastConfig());
    }
}
