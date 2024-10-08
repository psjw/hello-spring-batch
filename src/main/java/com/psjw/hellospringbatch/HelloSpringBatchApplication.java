package com.psjw.hellospringbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class HelloSpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloSpringBatchApplication.class, args);
    }

}
