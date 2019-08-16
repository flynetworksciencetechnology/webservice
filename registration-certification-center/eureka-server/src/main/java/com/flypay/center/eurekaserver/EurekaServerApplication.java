package com.flypay.center.eurekaserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 服务注册中心
 * 地址: http://localhost:8761/eureka/
 * 注意事项: EurekaServerApplication.java一定要在com.maven.xm包或者子包下,不然扫描不到
 * @author ouyangjun
 *
 * @SpringBootApplication 是 @Configuration、@EnableAutoConfiguration、@ComponentScan注解简化
 * @EnableEurekaServer 该注解表明应用为eureka服务，有可以联合多个服务作为集群，对外提供服务注册以及发现功能
 */

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaServerApplication.class);
    public static void main(String[] args) {
        // Spring Boot的SpringApplication类，用以启动一个Spring应用，实质上是为Spring应用创建并初始化Spring上下文。
        SpringApplication.run(EurekaServerApplication.class);
        LOGGER.info("********************Eureka Server Started********************");
    }
}
