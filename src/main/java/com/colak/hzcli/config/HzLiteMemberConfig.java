package com.colak.hzcli.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(Profiles.LITE_MEMBER)
@Slf4j
public class HzLiteMemberConfig {

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setLiteMember(true);
        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        log.info("Lite member profile is active.");
        // The lite member version must match the cluster version
        return Hazelcast.newHazelcastInstance(hazelcastConfig());
    }
}
