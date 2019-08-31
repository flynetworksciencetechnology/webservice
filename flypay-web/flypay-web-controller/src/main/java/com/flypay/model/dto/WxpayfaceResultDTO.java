package com.flypay.model.dto;

import com.flypay.annotation.XStreamCDATA;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WxpayfaceResultDTO{

    @XStreamAlias("return_code")
    public String returnCode;
    @XStreamAlias("return_msg")
    public String returnMsg;
    @XStreamAlias("appid")
    public String appid;
    @XStreamAlias("sub_appid")
    public String subAppid;
    @XStreamAlias("mch_id")
    public String mchid;
    @XStreamAlias("sub_mch_id")
    public String subMchid;
    @XStreamAlias("device_info")
    public String device_info;
    @XStreamAlias("nonce_str")
    public String nonceStr;
    @XStreamAlias("sign")
    public String sign;
    @XStreamAlias("result_code")
    public String resultCode;
    @XStreamAlias("err_code")
    public String errCode;
    @XStreamAlias("err_code_des")
    public String errCodedes;
    @XStreamAlias("openid")
    public String openid;
    @XStreamAlias("is_subscribe")
    public String isSubscribe;
    @XStreamAlias("sub_openid")
    public String subOpenid;
    @XStreamAlias("sub_is_subscribe")
    public String subIsSubscribe;
    @XStreamAlias("trade_type")
    public String tradeType;
    @XStreamAlias("fee_type")
    public String feeType;
    @XStreamAlias("total_fee")
    public String totalFee;
    @XStreamAlias("cash_fee_type")
    public String cashFeeType;
    @XStreamAlias("cash_fee")
    public String cash_fee;
    @XStreamAlias("transaction_id")
    public String transactionId;
    @XStreamAlias("out_trade_no")
    public String orderno;
    @XStreamAlias("detail")
    public String detail;
    @XStreamAlias("attach")
    public String attach;
    @XStreamAlias("promotion_detail")
    public String promotionDetail;
    @XStreamAlias("time_end")
    public String timeEnd;
    @XStreamAlias("bank_type")
    public String bank_type;
}
