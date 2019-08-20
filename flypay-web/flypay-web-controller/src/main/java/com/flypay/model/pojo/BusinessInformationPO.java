package com.flypay.model.pojo;

import javax.persistence.*;

@Entity
@Table(name = "business_info")
public class BusinessInformationPO extends BasePO{


    /**
     * 商户名字
     */
    @Column(name = "store_name",nullable = false)
    public String storeName;
    /**
     * 商户id
     */
    @Column(name = "store_id",nullable = false)
    public String storeId;
    /**
     * 终端设备编号，由商户定义
     */
    @Column(name = "device_id",nullable = false, unique = true)
    public String deviceId;
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
     * 设备串号
     */
    @Column(name = "uuid",nullable = false, unique = true)
    public String uuid;
    /**
     * //注：key为商户平台设置的密钥key
     */
    @Column(name = "MD5_key",nullable = false)
    public String MD5Key;
}
