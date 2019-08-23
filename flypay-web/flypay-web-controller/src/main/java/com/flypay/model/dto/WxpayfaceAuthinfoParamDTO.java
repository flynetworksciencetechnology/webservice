package com.flypay.model.dto;

import com.flypay.utils.CommonUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WxpayfaceAuthinfoParamDTO {

    /**
     * 门店编号，由商户定义， 各门店唯一。
     */
    @XStreamAlias("store_id")
    public String storeId;
    /**
     * 门店名称，由商户定义。（可用于展示）
     */
    @XStreamAlias("store_name")
    public String storeName;
    /**
     * 门店编号，由商户定义， 各门店唯一。
     */
    @XStreamAlias("device_id")
    public String deviceId;
    /**
     * 附加字段。字段格式使用Json
     */
    @XStreamAlias("attach")
    public String attach;
    /**
     * 初始化数据。由微信人脸SDK的接口返回。
     */
    @XStreamAlias("rawdata")
    public String rawdata;
    /**
     * 商户号绑定的公众号/小程序 appid
     */
    @XStreamAlias("appid")
    public String appid;
    /**
     * 商户号
     */
    @XStreamAlias("mch_id")
    public String mchId;
    /**
     * 子商户绑定的公众号/小程序 appid(服务商模式)
     */
    @XStreamAlias("sub_appid")
    public String subAppid;
    /**
     * 子商户号(服务商模式)。
     */
    @XStreamAlias("sub_mch_id")
    public String subMchid;
    /**
     * 取当前时间，10位unix时间戳。 例如：1239878956
     */
    @XStreamAlias("now")
    public String now;
    /**
     * 版本号。固定为1
     */
    @XStreamAlias("version")
    public String version;
    /**
     * 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
     */
    @XStreamAlias("sign_type")
    public String signType;
    /**
     * 随机字符串，不长于32位
     */
    @XStreamAlias("nonce_str")
    public String nonceStr;
    /**
     * 签名
     */
    @XStreamAlias("sign")
    public String sign;
    public WxpayfaceAuthinfoParamDTO() {
        this.version = "1";
        this.signType = "MD5";
        this.now = String.valueOf(System.currentTimeMillis());
        this.nonceStr = CommonUtils.getRandomString(32);
    }

    public WxpayfaceAuthinfoParamDTO(String storeId, String storeName, String deviceId, String rawdata, String appid, String mchId ,String subAppid, String subMchid) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.deviceId = deviceId;
        this.rawdata = rawdata;
        this.appid = appid;
        this.mchId = mchId;
        this.subAppid =subAppid;
        this.subMchid = subMchid;
        this.version = "1";
        this.signType = "MD5";
        this.now = String.valueOf(System.currentTimeMillis());
        this.nonceStr = CommonUtils.getRandomString(32);
    }

    @Override
    public String toString() {
        return "WxpayfaceAuthinfoParamDTO{" +
                "storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", attach='" + attach + '\'' +
                ", rawdata='" + rawdata + '\'' +
                ", appid='" + appid + '\'' +
                ", mchId='" + mchId + '\'' +
                ", subAppid='" + subAppid + '\'' +
                ", subMchId='" + subMchid + '\'' +
                ", now='" + now + '\'' +
                ", version='" + version + '\'' +
                ", signType='" + signType + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
