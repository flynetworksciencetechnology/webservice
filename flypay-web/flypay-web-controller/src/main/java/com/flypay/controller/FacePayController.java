package com.flypay.controller;

import com.flypay.model.Result;
import com.flypay.model.vo.BusinessInformationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="提供刷脸支付相关接口")
@RestController
@RequestMapping(value="/fly/pay")
public class FacePayController {

    @ApiOperation(value="初始化设备,根据设备编号,获取设备绑定的商户信息", notes="Test")
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public Result init(@RequestParam("uuid") String uuid){
        System.out.println("payType :" + uuid);

        //做模拟数据
        BusinessInformationVO biVo = new BusinessInformationVO();
        biVo.storeName = "";
        biVo.storeId = "";
        biVo.deviceId = "";
        biVo.appid = "";
        biVo.mchId = "";
        Result result = new Result();
        result.code = "0000";
        result.message = "获取成功";
        result.data = biVo;
        return result;
    }
}
