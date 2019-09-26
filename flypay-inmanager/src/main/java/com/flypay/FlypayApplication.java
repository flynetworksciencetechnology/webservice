package com.flypay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动程序
 * 
 * @author flypay
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableAsync
public class FlypayApplication {
    private static final Logger logger = LoggerFactory.getLogger("FlypayApplication");
    public static void main(String[] args){
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(FlypayApplication.class, args);
        logger.info("(♥◠‿◠)ﾉﾞ  飞付科技后台系统启动成功   ლ(´ڡ`ლ)ﾞ");
        logger.info(" .-------.   .-. 		 ____     __    ");
        logger.info(" |-------|   |-| 		 \\   \\   /  /");
        logger.info(" |-|         |-|			   \\  _. /  '   ");
        logger.info(" .-------.   |-|			    _( )_ .'     ");
        logger.info(" |-------|   |-|			___(_ o _)'      ");
        logger.info(" |-|         |-|			||   |(_,_)'     ");
        logger.info(" |-|         |-|		    |   `-'  /		 ");
        logger.info(" |-|         |_._______.	 \\      /       ");
        logger.info(" |-|		    |_._______|    `-..-'        ");
    }
}