package com.flypay.wechatpay;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.flypay")
public class WechatPayApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatPayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WechatPayApplication.class);
        LOGGER.info("********************Wechat Server Started********************");
    }

}