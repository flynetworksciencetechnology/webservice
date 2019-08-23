package com.flypay.model.pojo;

import javax.persistence.*;

/**
 * 商户信息
 */
@Entity
@Table(name = "merchan_info")
public class MerchanInfoPO extends BasePO{
    /**
     * 商户名字
     */
    @Column(name = "merchan_name",nullable = false)
    public String merchanName;
    /**
     * 商户号绑定的公众号/小程序 appid
     */
    @Column(name = "app_id")
    public String appid;
    /**
     * 商户号
     */
    @Column(name = "mch_id",nullable = false)
    public String mchId;
    /**
     * //注：key为商户平台设置的密钥key
     */
    @Column(name = "MD5_key")
    public String key;
    /**
     * 所属服务商id
     */
    @Column(name = "service_id")
    public Long serviceId;

}
