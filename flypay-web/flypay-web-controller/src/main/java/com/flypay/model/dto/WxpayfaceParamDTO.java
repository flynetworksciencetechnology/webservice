package com.flypay.model.dto;

import com.flypay.annotation.XStreamCDATA;
import com.flypay.utils.CommonUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WxpayfaceParamDTO {

    @XStreamAlias("appid")

    public String appid;
    @XStreamAlias("sub_appid")
    public String subAppid;
    @XStreamAlias("mch_id")
    public String mchid;
    @XStreamAlias("sub_mch_id")
    public String subMchid;
    @XStreamAlias("device_info")
    public String deviceId;
    @XStreamAlias("nonce_str")
    public String nonceStr;
    @XStreamAlias("sign")
    public String sign;
    @XStreamAlias("body")
    public String boby;
    @XStreamAlias("detail")
    @XStreamCDATA
    public String detail;
    @XStreamAlias("attach")
    public String attach;
    @XStreamAlias("out_trade_no")
    public String orderno;
    @XStreamAlias("total_fee")
    public Integer totalFee;
    @XStreamAlias("fee_type")
    public String feeType;
    @XStreamAlias("spbill_create_ip")
    public String spbillCreateIp;
    @XStreamAlias("goods_tag")
    public String goodsTag;
    @XStreamAlias("openid")
    @XStreamCDATA
    public String openid;
    @XStreamAlias("face_code")
    public String faceCode;
    @XStreamAlias("time_expire")
    public String timeExpire;

    public WxpayfaceParamDTO() {
        this.nonceStr = CommonUtils.getRandomString(32);
        this.feeType = "CNY";
    }
}
