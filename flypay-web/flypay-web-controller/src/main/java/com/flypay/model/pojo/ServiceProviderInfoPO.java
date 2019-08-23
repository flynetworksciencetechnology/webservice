package com.flypay.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务商表
 */
@Entity
@Table(name = "service_provider_info")
public class ServiceProviderInfoPO extends BasePO{
    /**
     * 商户名字
     */
    @Column(name = "service_name",nullable = false)
    public String serviceName;
    /**
     * 商户号绑定的公众号/小程序 appid
     */
    @Column(name = "app_id",nullable = false)
    public String appid;
    /**
     * 商户号
     */
    @Column(name = "mch_id",nullable = false)
    public String mchId;
    /**
     * //注：key为商户平台设置的密钥key
     */
    @Column(name = "MD5_key",nullable = false)
    public String key;
}
