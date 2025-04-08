package com.timeAuction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TimeProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeProductApplication.class, args);
    }
}