package com.example.travelportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class TravelPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelPortalApplication.class, args);
    }

}
