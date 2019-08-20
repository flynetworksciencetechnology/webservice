package com.flypay.utils.httpclient;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
public class MyPoolingHttpClientConnectionManager {
	/**
	 * 连接池最大连接数
	 */
	@Value("${httpclient.config.connMaxTotal}")
	private int connMaxTotal = 20;
	
	/**
	 * 
	 */
	@Value("${httpclient.config.maxPerRoute}")
	private int maxPerRoute = 20;
 
        /**
     * 连接存活时间，单位为s
     */
	@Value("${httpclient.config.timeToLive}")
    private int timeToLive = 60;

    @Bean(name = "MyPoolingHttpClientConnectionManager")
	public PoolingHttpClientConnectionManager poolingClientConnectionManager(){
		PoolingHttpClientConnectionManager poolHttpcConnManager = new PoolingHttpClientConnectionManager(getDefaultRegistry(), null, null ,null,60, TimeUnit.SECONDS);
		// 最大连接数
		poolHttpcConnManager.setMaxTotal(this.connMaxTotal);
		// 路由基数
		poolHttpcConnManager.setDefaultMaxPerRoute(this.maxPerRoute);
		return poolHttpcConnManager;
	}
	private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
		return RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory())
				.build();
	}
}