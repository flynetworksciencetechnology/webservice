package com.flypay.controller;

import com.flypay.bp.PayService;
import com.flypay.model.Result;
import com.flypay.model.dao.OrderInfoDao;
import com.flypay.model.pojo.OrderInfoPO;
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

import javax.transaction.Transactional;

@Api(tags="提供刷脸支付相关接口")
@RestController
@RequestMapping(value="/fly/pay")
public class FacePayController {
    private static final Logger LOGGER = Logger.getLogger(FacePayController.class);
    @Autowired
    PayService pay;

    /**
     * 第一次初始化,将uuid入库
     * 第二次查询出绑定的门店信息,存入缓存
      * @param uuid
     * @param ip
     * @return
     */
    @ApiOperation(value="初始化设备,根据设备编号,获取设备绑定的商户信息", notes="Test")
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public Result init(@RequestParam("uuid") String uuid, @RequestParam("ip") String ip){
        Result result = null;
        LOGGER.info("设备的UUID是 : " + uuid);
        try{
            result = pay.initEquipmentInfo(uuid,ip);
        }catch (Exception e){
            LOGGER.error("系统异常,初始化设备失败",e);
        }
        if( result == null){
            result.code = "-1111";
            result.message = "系统异常,初始化设备失败";
        }
        return result;
    }
    @ApiOperation(value="生成订单", notes="Test")
    @RequestMapping(value = "/creatorder", method = RequestMethod.POST)
    public Result creatorder(@RequestParam("uuid") String uuid,@RequestParam("amount") String amount){
        Result result = null;
        try{
            result = pay.creatorder(uuid,amount);
        }catch (Exception e){
            LOGGER.error("系统异常,生成订单失败",e);
        }
        if( result == null){
            result.code = "-1111";
            result.message = "系统异常,生成订单失败";
        }
        return result;
    }

    /**
     * 获取调用认证,从redis中获取调用参数,获取到调用凭证放redis,备用
     * @param rawdata
     * @param uuid
     * @return
     */
    @ApiOperation(value="获取微信人脸支付凭证", notes="Test")
    @RequestMapping(value = "/wechat/authinfo", method = RequestMethod.POST)
    public Result getWxpayfaceAuthinfo(@RequestParam("rawdata") String rawdata,@RequestParam("uuid") String uuid,String orderno){

        Result result = null;
        try{
            result = pay.getWxpayfaceAuthinfo(uuid,rawdata,orderno);
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
    @RequestMapping(value = "/topay", method = RequestMethod.POST)
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
