package com.flypay.controller;

import com.flypay.bp.PayService;
import com.flypay.model.Result;
import com.flypay.utils.DBUtils;
import com.flypay.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="提供刷脸支付相关接口")
@RestController
@RequestMapping(value="/fly/pay")
public class FacePayController {
    private static final Logger LOGGER = Logger.getLogger(FacePayController.class);
    @Autowired
    PayService pay;
    @Autowired
    DBUtils db;
    @Autowired
    RedisUtils redisUtils;
    @ApiOperation(value="初始化设备,根据设备编号,获取设备绑定的商户信息", notes="Test")
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public Result init(@RequestParam("uuid") String uuid){
        Result result = null;
        LOGGER.info("设备的UUID是 : " + uuid);
        try{
            result = pay.initEquipmentInfo(uuid);
        }catch (Exception e){
            LOGGER.error("系统异常,初始化设备失败",e);
        }
        if( result == null){
            result.code = "-1111";
            result.message = "系统异常,初始化设备失败";
        }
        return result;
    }
    @ApiOperation(value="初始化设备,根据设备编号,获取设备绑定的商户信息", notes="Test")
    @RequestMapping(value = "/getStoreMerchanInfo", method = RequestMethod.GET)
    public Result getStoreMerchanInfo(@RequestParam("uuid") String uuid,@RequestParam("ip") String ip){
        Result result = null;
        LOGGER.info("设备的UUID是 : " + uuid);
        try{
            result = pay.getStoreMerchanInfo(uuid,ip);
        }catch (Exception e){
            LOGGER.error("系统异常,获取商户信息失败失败",e);
        }
        if( result == null){
            result.code = "-1111";
            result.message = "系统异常,获取商户信息失败失败";
        }
        return result;
    }
    @ApiOperation(value="初始化设备,根据设备编号,获取设备绑定的商户信息", notes="Test")
    @RequestMapping(value = "/setRawdata", method = RequestMethod.GET)
    public Result setRawdata(@RequestParam("uuid") String uuid,@RequestParam("rawdata") String rawdata){
        Result result = null;
        LOGGER.info("设备的UUID是 : " + uuid);
        try{
            result = pay.setRawdata(uuid,rawdata);
        }catch (Exception e){
            LOGGER.error("系统异常,设置rawdata失败",e);
        }
        if( result == null){
            result.code = "-1111";
            result.message = "系统异常,设置rawdata失败";
        }
        return result;
    }

    @ApiOperation(value="获取微信人脸支付凭证", notes="Test")
    @RequestMapping(value = "/wechat/authinfo", method = RequestMethod.GET)
    public Result getWxpayfaceAuthinfo(@RequestParam("uuid") String uuid,@RequestParam("amount") String amount){

        Result result = null;
        try{
            result = pay.getWxpayfaceAuthinfo(uuid,amount,null);
        }catch (Exception e){
            LOGGER.error("系统异常,获取人脸支付认证失败",e);
        }
        if( result == null){
            result.code = "-1111";
            result.message = "系统异常,获取人脸支付认证失败";
        }
        return result;
    }

    /**
     * 支付
     * @return
     */
    @ApiOperation(value="进行人脸支付", notes="Test")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public Result pay(@RequestParam("uuid") String uuid,@RequestParam("openid") String openid,@RequestParam("faceCode") String faceCode,@RequestParam("orderno") String orderno){
        Result result = null;

        try{
            result = pay.pay(uuid,openid,faceCode,orderno);
        }catch (Exception e){
            LOGGER.error("系统异常,人脸支付失败",e);
        }
        if( result == null){
            result.code = "-1111";
            result.message = "系统异常,人脸支付失败";
        }
        return result;
    }
}
