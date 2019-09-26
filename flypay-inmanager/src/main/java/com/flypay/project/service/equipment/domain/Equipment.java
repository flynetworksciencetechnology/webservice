package com.flypay.project.service.equipment.domain;

import com.flypay.framework.aspectj.lang.annotation.Excel;
import com.flypay.framework.web.domain.BaseEntity;

import javax.validation.constraints.NotBlank;

public class Equipment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 设备id */
    @Excel(name = "设备id", cellType = Excel.ColumnType.NUMERIC)
    private Long equipmentId;
    /** 设备排序 */
    @Excel(name = "设备排序")
    private String equipmentSort;
    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceId;
    /** 角色状态（0正常 1停用） */
    @Excel(name = "设备状态", readConverterExp = "0=正常,1=停用")
    private String status;
    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    @Excel(name = "设备类型", readConverterExp = "0=微信非开发版,1=微信开发版,2=支付宝")
    private String type;
    /** 所属服务商 */
    @Excel(name = "设备所属服务商")
    private Long providerId;
    /** 所属服务商 */
    private String providerName;
    /** 是否被绑定 */
    @Excel(name = "是否被绑定", readConverterExp = "0=未绑定,1=绑定")
    private String isBand;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentSort() {
        return equipmentSort;
    }

    public void setEquipmentSort(String equipmentSort) {
        this.equipmentSort = equipmentSort;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getIsBand() {
        return isBand;
    }

    public void setIsBand(String isBand) {
        this.isBand = isBand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
