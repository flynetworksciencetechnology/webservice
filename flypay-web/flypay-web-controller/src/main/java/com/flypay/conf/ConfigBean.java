package com.flypay.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



/**
 * 读取配置文件
 */
@Component
public class ConfigBean {

    /**
     * 是否模拟
     */
    @Value("${moke}")
    public Boolean moke;
    /**
     * 区域码 固定
     */
    @Value("${areaCode}")
    public String areaCode;
    /**
     * 区域码 固定
     */
    @Value("${wechat.facepay.wxpayface_authinfo}")
    public String wxpayfaceAuthinfoURL;

    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public int port;

    @Value("${spring.redis.timeout}")
    public int timeout;

    @Value("${spring.redis.pool.max-active}")
    public int maxActive;

    @Value("${spring.redis.pool.max-idle}")
    public int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    public int minIdle;

    @Value("${spring.redis.pool.max-wait}")
    public long maxWaitMillis;
    @Value("${spring.redis.password}")
    public String redispwd;
}
