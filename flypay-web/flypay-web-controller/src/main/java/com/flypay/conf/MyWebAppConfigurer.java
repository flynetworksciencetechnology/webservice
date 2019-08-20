package com.flypay.conf;

import java.nio.charset.Charset;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurationSupport{
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // TODO Auto-generated method stub
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }
    @Bean
    public HttpMessageConverter responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }
}