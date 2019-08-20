package com.flypay.bp.impl;

import com.flypay.bp.PayService;
import com.flypay.conf.ConfigBean;
import com.flypay.model.Result;
import com.flypay.model.dto.WxpayfaceAuthinfoParamDTO;
import com.flypay.model.dto.WxpayfaceAuthinfoResultDTO;
import com.flypay.model.pojo.BusinessInformationPO;
import com.flypay.model.repository.BusinessInfoRepository;
import com.flypay.model.vo.BusinessInformationVO;
import com.flypay.utils.CommonUtils;
import com.flypay.utils.HttpUtils;
import com.flypay.utils.StringUtil;
import com.flypay.utils.XsteamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {

        @Autowired
        BusinessInfoRepository bi;
        @Autowired
        ConfigBean conf;
        @Override
        public Result getBusinessInformation(String uuid) {
                Result result = new Result();
                if(!StringUtil.hasText(uuid)){
                        result.code = "-1111";
                        result.message = "UUID为空,请联系开发人员";
                        return  result;
                }

                if( conf.moke){
                        //做模拟数据
                        BusinessInformationVO biVo = new BusinessInformationVO();
                        biVo.storeName = "测试公司";
                        biVo.storeId = "123445";
                        biVo.deviceId = "12312512312";
                        biVo.appid = "123123215123123";
                        biVo.mchId = "12312541251";
                        biVo.key = "123123213123";
                        result.code = "0000";
                        result.message = "获取成功";
                        result.data = biVo;
                }else{
                        //从库里查询真实数据
                        BusinessInformationPO biPo = bi.findByUuidEquals(uuid);
                        if( biPo == null){
                                result.code = "-1111";
                                result.message = "该设备 :" + uuid + "无商户进行授权";
                        }else{
                                BusinessInformationVO biVo = new BusinessInformationVO();
                                biVo.storeName =biPo.storeName;
                                biVo.storeId = biPo.storeId;
                                biVo.deviceId = biPo.deviceId;
                                biVo.appid = biPo.appid;
                                biVo.mchId = biPo.mchId;
                                biVo.uuid = biPo.uuid;
                                biVo.key = biPo.MD5Key;
                                result.code = "0000";
                                result.message = "获取成功";
                                result.data = biVo;
                        }
                }
                return result;
        }

        @Override
        public Result getWxpayfaceAuthinfo(String storeId, String storeName, String deviceId, String rawdata, String appid, String mchId,String key) {
                Result result = new Result();
                if(!StringUtil.hasText(storeId)){
                        result.code = "-1111";
                        result.message = "商户id为空,请联系开发人员";
                        return  result;
                }
                if(!StringUtil.hasText(storeName)){
                        result.code = "-1111";
                        result.message = "门店编号为空,请联系开发人员";
                        return  result;
                }
                if(!StringUtil.hasText(deviceId)){
                        result.code = "-1111";
                        result.message = "终端设备编号为空,请联系开发人员";
                        return  result;
                }
                if(!StringUtil.hasText(rawdata)){
                        result.code = "-1111";
                        result.message = "初始化数据为空,请联系开发人员";
                        return  result;
                }
                if(!StringUtil.hasText(appid)){
                        result.code = "-1111";
                        result.message = "商户绑定的公众号/小程序id为空,请联系开发人员";
                        return  result;
                }
                if(!StringUtil.hasText(mchId)){
                        result.code = "-1111";
                        result.message = "商户号为空,请联系开发人员";
                        return  result;
                }
                if( conf.moke){
                        //模拟数据
                        WxpayfaceAuthinfoResultDTO response = new WxpayfaceAuthinfoResultDTO();
                        response.returnCode = "SUCCESS";
                        response.returnMsg = "请求成功";
                        response.authinfo = "q3OPhFtQBf6KZGqmZhejKCRy5K/ch0kwS11YSsEj9XmUGqcsT2QPHt0Oa7xaCMCoSZTWMmShCo4dOiO5tU+OJEsvSxXzn5m3Nkh747tinNlbpJmVq1zOPj+FJNndkzanxoiAddO8p1EfrmUhJs/aNf0pDfrPoVfkAapK+ZY6blwyaDQ9bB7+KkZq29kObsXOZ3thg+bxP4RAqC0oxNS4JiyP0uA1Euzxtkc9lCTebloFied8stILrMehUKukeMGkZ1SzTyc8/HFHApzHahNPX6yD8ttzYnhe+IRMFJgpuTlIvEOYZUxenPXE1A5clrPvOBeJDszX/OvZl4fpYWPpXAcVQlw+gfYhblt+rT6ALMsD73w/rT4NRriQEEraC4Pfb5yua4qAqv4TVo04";
                        response.expiresIn = 7200;
                        response.nonceStr = "Tivppi4UXAbgLxk8e1Sij76YdowOFFii";
                        response.sign = "PL0EUID6A7ICWNKHCSMQC0UIXOYNSE5B";
                        response.appid = "wx31fdaErqR31";
                        response.mchId = "12345689";
                        result.code = "0000";
                        result.message = "获取人脸支付认证信息成功";
                        result.data = response;
                }else{
                        //去请求微信
                        //准备参数
                        WxpayfaceAuthinfoParamDTO wxParam = new WxpayfaceAuthinfoParamDTO(storeId,storeName,deviceId,rawdata,appid,mchId);
                        //获取签名
                        wxParam.sign = CommonUtils.sign(wxParam,WxpayfaceAuthinfoParamDTO.class,key);
                        //调用方法
                        String res = HttpUtils.sendRequest(HttpUtils.Method.POST,conf.wxpayfaceAuthinfoURL,null,XsteamUtil.toXml(wxParam,WxpayfaceAuthinfoParamDTO.class), HttpUtils.Encode.UTF8);
                        if( !StringUtil.hasText(res)){
                                result.code = "-1111";
                                result.message = "获取人脸支付认证失败,请检查网络信息";
                                return  result;
                        }
                        //将返回的xml转成对象
                        WxpayfaceAuthinfoResultDTO response = XsteamUtil.toBean(WxpayfaceAuthinfoResultDTO.class,res);
                        if( response != null && "SUCCESS".equals(response.returnCode)){
                                result.code = "0000";
                                result.message = "获取人脸支付认证信息成功";
                                result.data = response;
                        }else{
                                result.code = "-1111";
                                result.message = "获取人脸支付认证失败,请检查网络信息";
                                if( response != null){
                                        result.message = response.returnMsg;
                                }
                        }
                }
                return result;
        }
}
