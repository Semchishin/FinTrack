package com.semchishin.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.semchishin.api", "semchishin.core"})
public class FinTrackApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinTrackApplication.class, args);
    }
}
