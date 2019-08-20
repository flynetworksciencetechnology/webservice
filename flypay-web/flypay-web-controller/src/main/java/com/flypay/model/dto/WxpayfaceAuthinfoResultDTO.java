package com.flypay.model.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class WxpayfaceAuthinfoResultDTO {

    /**
     * 错误码
     */
    @XStreamAlias("return_code")
    public String returnCode;

    /**
     * 对错误码的描述
     */
    @XStreamAlias("return_msg")
    public String returnMsg;
    /**
     * SDK调用凭证。用于调用SDK的人脸识别接口。
     */
    @XStreamAlias("authinfo")
    public String authinfo;
    /**
     * authinfo的有效时间, 单位秒。 例如: 3600
     * 在有效时间内, 对于同一台终端设备，
     * 相同的参数的前提下(如：相同的公众号、商户号、 门店编号等），
     * 可以用同一个authinfo，
     * 多次调用SDK的getWxpayfaceCode接口。
     */
    @XStreamAlias("expires_in")
    public Integer expiresIn;
    /**
     * 	随机字符串
     */
    @XStreamAlias("nonce_str")
    public String nonceStr;
    /**
     * 响应结果签名
     */
    @XStreamAlias("sign")
    public String sign;
    /**
     * 公众号
     */
    @XStreamAlias("appid")
    public String appid;
    /**
     * 商户号
     */
    @XStreamAlias("mch_id")
    public String mchId;
    /**
     * 子商户公众账号ID(服务商模式)
     */
    @XStreamAlias("sub_appid")
    public String subAppid;
    /**
     * 子商户号(服务商模式)
     */
    @XStreamAlias("sub_mch_id")
    public String subMchId;
}
