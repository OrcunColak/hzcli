package com.colak.hzcli;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HzcliApplication {

    @Autowired
    HazelcastInstance hazelcastClient;

    public static void main(String[] args) {
        SpringApplication.run(HzcliApplication.class, args);
    }

    @PostConstruct
    public void commandLineRunner() {

            IMap<Object, Object> map = hazelcastClient.getMap("mymap");
            map.put(1,"1");
        ;
    }


}
