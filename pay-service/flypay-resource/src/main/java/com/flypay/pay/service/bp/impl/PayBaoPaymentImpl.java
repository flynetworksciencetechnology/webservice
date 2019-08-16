package com.flypay.pay.service.bp.impl;

import com.flypay.center.api.conf.StaticConf;
import com.flypay.center.api.wechat.PayApi;
import com.flypay.pay.service.bp.PaymentPaybaoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayBaoPaymentImpl implements PaymentPaybaoInterface {
    @Autowired
    private PayApi payApi;

    public String payment(StaticConf.PAY_KIND payKind) {

        return payApi.payment(payKind);
    }
}
