package com.treetalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableMongoAuditing
@EnableCaching
@EnableAsync
@EnableScheduling
public class TreeHoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreeHoleApplication.class, args);
    }
}