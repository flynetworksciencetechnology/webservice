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
}
