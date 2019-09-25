package com.flypay.project.service.merchant.domain;

import com.flypay.framework.aspectj.lang.annotation.Excel;
import com.flypay.framework.web.domain.BaseEntity;

import javax.validation.constraints.NotBlank;

public class Merchant extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 商户id */
    @Excel(name = "商户id", cellType = Excel.ColumnType.NUMERIC)
    private Long merchantId;
    /** 商户名称 */
    @Excel(name = "商户名称")
    private String merchantName;
    /** 商户排序 */
    @Excel(name = "商户排序")
    private String merchantSort;
    /** 商户绑定公众号id */
    @Excel(name = "商户绑定公众号id")
    private String appId;
    /** 商户支付key */
    @Excel(name = "商户支付key")
    private String MD5Key;
    /** 商户编号 */
    @Excel(name = "商户编号")
    private String mchId;
    /** 角色状态（0正常 1停用） */
    @Excel(name = "商户状态", readConverterExp = "0=正常,1=停用")
    private String status;
    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    /** 绑定的服务商id */
    private Long providerId;
    /** 绑定的服务商id */
    private String providerName;
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
    @NotBlank(message = "商户名称不能为空")
    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    @NotBlank(message = "显示顺序不能为空")
    public String getMerchantSort() {
        return merchantSort;
    }

    public void setMerchantSort(String merchantSort) {
        this.merchantSort = merchantSort;
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

    public Long  getProviderId() {
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
}
