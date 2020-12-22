package com.kakuom.finaltipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FinalTippingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalTippingApplication.class, args);
    }

}
