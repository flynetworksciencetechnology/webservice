package com.flypay.project.service.store.domain;

import com.flypay.framework.aspectj.lang.annotation.Excel;
import com.flypay.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 门店对象 service_store
 * 
 * @author flypay
 * @date 2019-09-27
 */
public class ServiceStore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 门店id */
    private Long storeId;

    /** 门店名称 */
    @Excel(name = "门店名称")
    private String storeName;

    /** 门店品牌 */
    @Excel(name = "门店品牌")
    private String brand;

    /** 门店地址 */
    @Excel(name = "门店地址")
    private String city;

    /** 绑定的设备 */
    @Excel(name = "绑定的设备")
    private Long equipmentId;

    /** 所属商户 */
    @Excel(name = "所属商户")
    private Long merchantId;
    private Long providerId;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 伤处标记 */
    private String delFlag;

    /** 创建人 */
    @Excel(name = "创建人")
    private String creatBy;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date creatTime;

    public void setStoreId(Long storeId)
    {
        this.storeId = storeId;
    }

    public Long getStoreId()
    {
        return storeId;
    }
    public void setStoreName(String storeName) 
    {
        this.storeName = storeName;
    }

    public String getStoreName() 
    {
        return storeName;
    }
    public void setBrand(String brand) 
    {
        this.brand = brand;
    }

    public String getBrand() 
    {
        return brand;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setEquipmentId(Long equipmentId) 
    {
        this.equipmentId = equipmentId;
    }

    public Long getEquipmentId() 
    {
        return equipmentId;
    }
    public void setMerchantId(Long merchantId) 
    {
        this.merchantId = merchantId;
    }

    public Long getMerchantId() 
    {
        return merchantId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setCreatBy(String creatBy) 
    {
        this.creatBy = creatBy;
    }

    public String getCreatBy() 
    {
        return creatBy;
    }
    public void setCreatTime(Date creatTime) 
    {
        this.creatTime = creatTime;
    }

    public Date getCreatTime() 
    {
        return creatTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getStoreId())
            .append("storeName", getStoreName())
            .append("brand", getBrand())
            .append("city", getCity())
            .append("equipmentId", getEquipmentId())
            .append("merchantId", getMerchantId())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("creatBy", getCreatBy())
            .append("creatTime", getCreatTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
