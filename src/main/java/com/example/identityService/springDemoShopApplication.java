package com.example.identityService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class springDemoShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(springDemoShopApplication.class, args);
    }
}

