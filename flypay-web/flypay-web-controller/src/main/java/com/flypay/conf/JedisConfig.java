package com.flypay.conf;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig extends CachingConfigurerSupport {
    private Logger logger = Logger.getLogger(JedisConfig.class);

    /**
     * SpringSession  需要注意的就是redis需要2.8以上版本，然后开启事件通知，在redis配置文件里面加上
     * notify-keyspace-events Ex
     * Keyspace notifications功能默认是关闭的（默认地，Keyspace 时间通知功能是禁用的，因为它或多或少会使用一些CPU的资源）。
     * 或是使用如下命令：
     * redis-cli config set notify-keyspace-events Egx
     * 如果你的Redis不是你自己维护的，比如你是使用阿里云的Redis数据库，你不能够更改它的配置，那么可以使用如下方法：在applicationContext.xml中配置
     * <util:constant static-field="org.springframework.session.data.redis.config.ConfigureRedisAction.NO_OP"/>
     * @return
     */
    @Autowired
    ConfigBean conf;
    @Bean
    public JedisPool redisPoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(conf.maxIdle);
        jedisPoolConfig.setMaxWaitMillis(conf.maxWaitMillis);
        jedisPoolConfig.setMaxTotal(conf.maxActive);
        jedisPoolConfig.setMinIdle(conf.minIdle);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,conf.host,conf.port,conf.timeout,conf.redispwd);

        logger.info("JedisPool注入成功！");
        return  jedisPool;
    }

}