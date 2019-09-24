package com.flypay.project.service.provider.domain;

import com.flypay.framework.aspectj.lang.annotation.Excel;
import com.flypay.framework.web.domain.BaseEntity;

import javax.validation.constraints.NotBlank;

public class Provider extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 服务商id */
    @Excel(name = "服务商id", cellType = Excel.ColumnType.NUMERIC)
    private Long providerId;
    /** 服务商名称 */
    @Excel(name = "服务商名称")
    private String providerName;
    /** 服务商排序 */
    @Excel(name = "服务商排序")
    private String providerSort;
    /** 服务商绑定公众号id */
    @Excel(name = "服务商绑定公众号id")
    private String appId;
    /** 服务商支付key */
    @Excel(name = "服务商支付key")
    private String MD5Key;
    /** 服务商编号 */
    @Excel(name = "服务商编号")
    private String mchId;
    /** 角色状态（0正常 1停用） */
    @Excel(name = "服务商状态", readConverterExp = "0=正常,1=停用")
    private String status;
    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }
    @NotBlank(message = "服务商名称不能为空")
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    @NotBlank(message = "显示顺序不能为空")
    public String getProviderSort() {
        return providerSort;
    }

    public void setProviderSort(String providerSort) {
        this.providerSort = providerSort;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMD5Key() {
        return MD5Key;
    }

    public void setMD5Key(String MD5Key) {
        this.MD5Key = MD5Key;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
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



}
