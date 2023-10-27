package com.colak.hzcli;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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
        IMap<String, String> map = hazelcastClient.getMap("testmap");
        map.put("1", "1");
    }
}
