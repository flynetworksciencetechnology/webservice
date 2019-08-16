package com.flypay.controller;
import com.flypay.bp.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.logging.Logger;

/**
 * @RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用
1) 如果只是使用@RestController注解Controller，则Controller中的方法无法返回jsp页面，或者html，配置的视图解析器 InternalResourceViewResolver不起作用，返回的内容就是Return 里的内容
2) 如果需要返回到指定页面，则需要用 @Controller配合视图解析器InternalResourceViewResolver才行。如果需要返回JSON，XML或自定义mediaType内容到页面，则需要在对应的方法上加上@ResponseBody注解。
 *
 * @author ouyangjun
 *
 */
@Api(tags="Controller")
@RestController
@RequestMapping(value="/fly/pay")
public class PayController {
    @Autowired
    private PayService psyService;

    @ApiOperation(value="直接调用", notes="Test")
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public String helloPay(){
        return psyService.helloPay();
    }

    /**
     * Fegin方式通信
     * @return
     */
    @ApiOperation(value="fegin通信Test、OAuth模块", notes="Test")
    @RequestMapping(value = "/fegin/pay", method = RequestMethod.GET)
    public String feginPay(@RequestParam("payType") String payType, @RequestParam("payKind") String payKind){
        System.out.println("payType :" + payType);
        System.out.println("payKind :" + payKind);
        return psyService.feginPay(payType,payKind);
    }

    /**
     * ribbon方式通信
     * @return
     */
    @ApiOperation(value="ribbon通信Test、OAuth模块", notes="Test")
    @RequestMapping(value = "/ribbon/pay", method = RequestMethod.GET)
    public String ribbonPay(){
        return psyService.ribbonPay();
    }
}
