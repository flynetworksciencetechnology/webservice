package com.flypay.pay.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.flypay")
public class ResourceApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class);
        LOGGER.info("********************OAuth Server Started********************");
    }

}
