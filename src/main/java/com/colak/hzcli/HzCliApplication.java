package com.colak.hzcli;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.spi.impl.executionservice.ExecutionService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class HzCliApplication {

    protected HazelcastInstance hazelcastClient;

    public static void main(String[] args) {
        SpringApplication.run(HzCliApplication.class, args);
    }

    @Autowired
    public void setHazelcastClient(HazelcastInstance hazelcastClient) {
        this.hazelcastClient = hazelcastClient;
    }

    @PostConstruct
    public void postConstruct() {
        IExecutorService executorService = hazelcastClient.getExecutorService(ExecutionService.SYSTEM_EXECUTOR);
//        IMap<String, String> map = hazelcastClient.getMap("testmap");
//        map.put("1", "1");
    }
}
