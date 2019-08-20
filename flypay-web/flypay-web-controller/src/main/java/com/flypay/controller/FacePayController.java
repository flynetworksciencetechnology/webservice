package com.flypay.controller;

import com.flypay.bp.PayService;
import com.flypay.model.Result;
import com.flypay.utils.DBUtils;
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
    @ApiOperation(value="初始化设备,根据设备编号,获取设备绑定的商户信息", notes="Test")
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public Result init(@RequestParam("uuid") String uuid){
        Result result = new Result();
        LOGGER.info("设备的UUID是 : " + uuid);
        try{
            result = pay.getBusinessInformation(uuid);
        }catch (Exception e){
            LOGGER.error("系统异常,初始化设备失败",e);
        }
        if( result == null){
            result.code = "-1111";
            result.message = "系统异常,初始化设备失败";
        }
        return result;
    }

    @ApiOperation(value="获取微信人脸支付凭证", notes="Test")
    @RequestMapping(value = "/wechat/authinfo", method = RequestMethod.POST)
    public Result getWxpayfaceAuthinfo(@RequestParam("storeId") String storeId, @RequestParam("storeId") String storeName,
            @RequestParam("deviceId") String deviceId,@RequestParam("rawdata") String rawdata,@RequestParam("appid") String appid,
            @RequestParam("mchId") String mchId,@RequestParam("key") String key){

        Result result = new Result();
        try{
            result = pay.getWxpayfaceAuthinfo(storeId,storeName,deviceId,rawdata,appid,mchId,key);
        }catch (Exception e){
            LOGGER.error("系统异常,获取人脸支付认证失败",e);
        }
        if( result == null){
            result.code = "-1111";
            result.message = "系统异常,获取人脸支付认证失败";
        }
        return result;
    }
    @ApiOperation(value="获取微信人脸支付凭证", notes="Test")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result test(){

        Result result = new Result();

        Long 飞付科技 = db.creatId(db.id_bit, "飞付科技", DBUtils.TableIndex.business_info);
        result.code = "0000";
        result.message = "获取成功";
        result.data = 飞付科技;
        return result;
    }
}
