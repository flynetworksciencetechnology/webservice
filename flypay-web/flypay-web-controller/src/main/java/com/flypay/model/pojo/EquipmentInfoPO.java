package com.flypay.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设备信息
 */
@Entity
@Table(name = "equipment_info")
public class EquipmentInfoPO extends BasePO {

    /**
     * 设备唯一串号,从设备端获取
     */
    @Column(nullable = false,unique = true)
    public String uuid;
    /**
     * 设备类型,支付宝,微信等,通过数据字段去查,0
     */
    @Column
    public Integer type;
    @Column(name = "device_id")
    public String deviceId;


}
