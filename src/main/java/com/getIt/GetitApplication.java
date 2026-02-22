package com.getit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // <--- This is crucial
public class GetitApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetitApplication.class, args);
    }
}