package com.flypay.pay.service.bp.impl;

import com.flypay.center.api.conf.StaticConf;
import com.flypay.center.api.paybao.PayApi;
import com.flypay.pay.service.bp.PaymentWechatInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatPaymentImpl implements PaymentWechatInterface {
    @Autowired
    private PayApi payApi;
    @Override
    public String payment(StaticConf.PAY_KIND payKind) {

        return payApi.payment(payKind);
    }
}
