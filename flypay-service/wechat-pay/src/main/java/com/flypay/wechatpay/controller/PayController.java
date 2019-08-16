package com.flypay.wechatpay.controller;

import com.flypay.center.api.conf.StaticConf;
import com.flypay.wechatpay.bp.interfaceproxy.WechatPayInterfaceProxy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "flapay的一个模块")
@RestController
@RequestMapping(value = "/pay")
public class PayController {

    @ApiOperation(value="module模块,Test方法", notes="Test")
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public String payment(@RequestParam("payKind") StaticConf.PAY_KIND payKind){
        //获取参数,选择支付类型
        WechatPayInterfaceProxy wpProxy = new WechatPayInterfaceProxy(payKind);
        return wpProxy.pay();
    }
}
