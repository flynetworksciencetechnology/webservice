package com.flypay.pay.service.bp;


import com.flypay.center.api.conf.StaticConf;

public interface PaymentWechatInterface {

    public String payment(StaticConf.PAY_KIND payKind);
}
