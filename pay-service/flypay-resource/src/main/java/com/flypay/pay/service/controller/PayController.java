package com.flypay.pay.service.controller;

import com.flypay.center.api.conf.StaticConf;
import com.flypay.pay.service.bp.PaymentPaybaoInterface;
import com.flypay.pay.service.bp.PaymentWechatInterface;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "flapay的一个模块")
@RestController
@RequestMapping(value = "/payment")
public class PayController {

    @Autowired
    PaymentWechatInterface pw;

    @Autowired
    PaymentPaybaoInterface pp;
    @ApiOperation(value="微信支付", notes="Test")
    @RequestMapping(value = "/wechat", method = RequestMethod.POST)
    public String payment4Wechat(@RequestParam("payKind") StaticConf.PAY_KIND payKind){
        //获取参数,选择支付渠道
        return pw.payment(payKind);
    }
    @ApiOperation(value="微信支付", notes="Test")
    @RequestMapping(value = "/paybao", method = RequestMethod.POST)
    public String payment4PayBao(@RequestParam("payKind") StaticConf.PAY_KIND payKind){
        //获取参数,选择支付渠道
        return pp.payment(payKind);
    }
}
