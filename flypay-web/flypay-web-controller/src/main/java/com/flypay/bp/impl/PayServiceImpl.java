package com.flypay.bp.impl;

import com.flypay.bp.PayService;
import com.flypay.conf.ConfigBean;
import com.flypay.model.Result;
import com.flypay.model.dao.EquipmentInfoDAO;
import com.flypay.model.dto.WxpayfaceAuthinfoParamDTO;
import com.flypay.model.dto.WxpayfaceAuthinfoResultDTO;
import com.flypay.model.pojo.EquipmentInfoPO;
import com.flypay.model.vo.BusinessInformationVO;
import com.flypay.utils.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PayServiceImpl implements PayService {

        private static final Logger logger = Logger.getLogger(PayServiceImpl.class);
        @Autowired
        ConfigBean conf;
        @Autowired
        DBUtils db;
        @Autowired
        RedisUtils redis;
        @Autowired
        EquipmentInfoDAO eDao;
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
                        biVo.storeName = "测试商户";
                        biVo.storeId = "6210000001";
                        biVo.deviceId = "6210000012";
                        biVo.appid = "wx5e5307635a345231";
                        biVo.mchid = "1490348042";
                        biVo.key = "5DD1959F432DE0F068AE8A1D84423008";
                        biVo.subAppid = "";
                        biVo.subMchid = "1528418631";
                        biVo.serviceName = "飞付科技";
                        biVo.serviceId = "6210000001";
                        result.code = "0000";
                        result.message = "获取成功";
                        result.data = biVo;
                }else{
                        //从0号reids库中获取信息
                        BusinessInformationVO biVo = redis.get(uuid,BusinessInformationVO.class, RedisUtils.RedisDBIndex.base);
                        if( biVo != null){
                                //获取成功
                                result.code = "0000";
                                result.message = "查询商户基本信息成功";
                                result.data = biVo;
                                return result;
                        }
                        //从库里查询真实数据//需要连表查询,通过uuid获取绑定商户,通过商户找到关联的服务商,生成一个设备信息的对象
                        StringBuilder sb = new StringBuilder();
                        sb.append("SELECT");
                        sb.append(" bi.store_name AS storeName,");
                        sb.append("	bi.store_id AS storeId,");
                        sb.append("	bi.app_id AS subAppid,");
                        sb.append("	bi.mch_id AS subMchid,");
                        sb.append("	ei.device_id AS deviceId,");
                        sb.append("	ei.uuid AS uuid,");
                        sb.append(" spi.serivce_name AS serviceName,");
                        sb.append(" spi.service_id AS serviceId,");
                        sb.append("	spi.MD5_key AS `key`,");
                        sb.append("	spi.app_id AS appid,");
                        sb.append(" spi.mch_id AS mchid");
                        sb.append(" FROM");
                        sb.append("	business_info bi");
                        sb.append(" LEFT JOIN business_equip be ON bi.id = be.business_id");
                        sb.append(" LEFT JOIN equipment_info ei ON ei.id = be.equipI_id");
                        sb.append(" LEFT JOIN service_business sb ON sb.business_id = bi.id");
                        sb.append(" LEFT JOIN service_provider_info spi ON spi.id = sb.service_id");
                        sb.append(" WHERE ei.uuid = ?");
                        try {
                                biVo = db.queryBySQL(BusinessInformationVO.class,sb.toString(),uuid);
                        } catch (Exception e) {
                                logger.error("查询商户信息失败",e);
                                result.code = "-1111";
                                result.message = "查询商户信息失败,请联系开发人员";
                                return  result;
                        }
                        if( biVo == null){
                                result.code = "-1111";
                                result.message = "该设备 :" + uuid + "无商户进行授权";
                        }else{
                                result.code = "0000";
                                result.message = "查询商户基本信息成功";
                                result.data = biVo;
                                //存入redis
                                redis.set(uuid,biVo, RedisUtils.RedisDBIndex.base,3600);
                        }
                }
                return result;
        }

        @Override
        public Result getWxpayfaceAuthinfo(String uuid) {
                Result result = new Result();
                if(!StringUtil.hasText(uuid)){
                        result.code = "-1111";
                        result.message = "uuid为空,请联系开发人员";
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
                        //从reids获取认证信息
                        String key = uuid + "_WXPAYFACE_AUTHINFO";
                        WxpayfaceAuthinfoResultDTO response = redis.get(key,WxpayfaceAuthinfoResultDTO.class, RedisUtils.RedisDBIndex.base);
                        if( response != null){
                                result.code = "0000";
                                result.message = "获取人脸支付认证信息成功";
                                result.data = response;
                                return result;
                        }
                        //去微信请求
                        //准备参数
                        //获取商户信息
                        Result businessRes = getBusinessInformation(uuid);
                        if( businessRes == null || "0000".equals(businessRes.code)){
                                result.code = "-111";
                                result.message = "获取人脸支付认证信息失败,请检查商户信息";
                                return result;
                        }
                        BusinessInformationVO biVO= (BusinessInformationVO)businessRes.data;
                        if(!StringUtil.hasText(biVO.storeName)){
                                result.code = "-1111";
                                result.message = "门店名称为空,请联系开发人员";
                                return  result;
                        }
                        if(!StringUtil.hasText(biVO.storeId)){
                                result.code = "-1111";
                                result.message = "门店编号为空,请联系开发人员";
                                return  result;
                        }
                        if(!StringUtil.hasText(biVO.deviceId)){
                                result.code = "-1111";
                                result.message = "终端设备编号为空,请联系开发人员";
                                return  result;
                        }
                        if(!StringUtil.hasText(biVO.rawdata)){
                                result.code = "-1111";
                                result.message = "初始化数据为空,请联系开发人员";
                                return  result;
                        }
                        if(!StringUtil.hasText(biVO.appid)){
                                result.code = "-1111";
                                result.message = "服务商绑定的公众号/小程序id为空,请联系开发人员";
                                return  result;
                        }
                        if(!StringUtil.hasText(biVO.mchid)){
                                result.code = "-1111";
                                result.message = "服务商编号为空,请联系开发人员";
                                return  result;
                        }
                        WxpayfaceAuthinfoParamDTO wxParam = new WxpayfaceAuthinfoParamDTO(biVO.storeId,biVO.storeName,biVO.deviceId,biVO.rawdata,biVO.appid,biVO.mchid,biVO.subAppid,biVO.subMchid);
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
                        response = XsteamUtil.toBean(WxpayfaceAuthinfoResultDTO.class,res);
                        if( response != null && "SUCCESS".equals(response.returnCode)){
                                result.code = "0000";
                                result.message = "获取人脸支付认证信息成功";
                                result.data = response;
                                //放入redis
                                Integer expiresIn = response.expiresIn;
                                redis.set(key,WxpayfaceAuthinfoResultDTO.class, RedisUtils.RedisDBIndex.base,(expiresIn - 60));
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

        /**
         * 初始化设备
         * @param uuid
         * @return
         */
        @Override
        @Transactional
        public Result initEquipmentInfo(String uuid) {
                Result result = new Result();
                if(!StringUtil.hasText(uuid)){
                        result.code = "-1111";
                        result.message = "UUID为空,请联系开发人员";
                        return  result;
                }
                try {
                        //创建设备对象
                        EquipmentInfoPO eInfo = new EquipmentInfoPO();
                        eInfo.id = db.creatId(db.id_bit,null,DBUtils.TableIndex.equipment_info);
                        eInfo.uuid = uuid;
                        eInfo.type = 0;
                        eInfo = eDao.save(eInfo);
                        result.code = "0000";
                        result.message = "初始化设备成功,设备信息已入库";
                        result.data = eInfo;
                } catch (Exception e) {
                        logger.error("初始化设备失败,请联系开发人员",e);
                }
                return result;
        }

        /**
         * 设置rawdata
         * @param uuid
         * @param rawdata
         * @return
         */
        @Override
        public Result setRawdata(String uuid, String rawdata) {
                Result result = new Result();
                if(!StringUtil.hasText(uuid)){
                        result.code = "-1111";
                        result.message = "uuid为空,请联系开发人员";
                        return  result;
                }
                if(!StringUtil.hasText(rawdata)){
                        result.code = "-1111";
                        result.message = "rawdata为空,请联系开发人员";
                        return  result;
                }
                //获取商户信息
                Result res = getBusinessInformation(uuid);
                if( res == null || "0000".equals(res.code)){
                        result.code = "-1111";
                        result.message = "获取人脸支付认证信息失败,请检查商户信息";
                        return result;
                }
                BusinessInformationVO biVO = (BusinessInformationVO)res.data;
                if( biVO == null){
                        result.code = "-1111";
                        result.message = "获取人脸支付认证信息失败,请检查商户信息";
                        return result;
                }
                biVO.rawdata = rawdata;
                //设置到redis
                redis.set(uuid,biVO, RedisUtils.RedisDBIndex.base,3600);
                result.code = "0000";
                result.message = "设置成功";
                return result;
        }
}
