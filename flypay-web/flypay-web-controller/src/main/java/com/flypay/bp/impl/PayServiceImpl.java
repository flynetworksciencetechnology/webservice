package com.flypay.bp.impl;

import com.flypay.bp.PayService;
import com.flypay.conf.ConfigBean;
import com.flypay.model.Result;
import com.flypay.model.dao.EquipmentInfoDAO;
import com.flypay.model.dao.OrderDetailInfoDao;
import com.flypay.model.dao.OrderInfoDao;
import com.flypay.model.dto.WxpayfaceAuthinfoParamDTO;
import com.flypay.model.dto.WxpayfaceAuthinfoResultDTO;
import com.flypay.model.dto.WxpayfaceParamDTO;
import com.flypay.model.dto.WxpayfaceResultDTO;
import com.flypay.model.pojo.EquipmentInfoPO;
import com.flypay.model.pojo.OrderDetailInfoPO;
import com.flypay.model.pojo.OrderInfoPO;
import com.flypay.model.vo.StoreMerchanEquipmentInfoVO;
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
        @Autowired
        OrderInfoDao orderDao;
        @Autowired
        OrderDetailInfoDao odDao;
        @Autowired
        HttpUtils httpUtils;
        @Override
        public Result getStoreMerchanInfo(String uuid, String ip) {
                Result result = new Result();
                if(!StringUtil.hasText(uuid)){
                        result.code = "-1111";
                        result.message = "UUID为空,请联系开发人员";
                        return  result;
                }

                if( conf.moke){
                        //做模拟数据
                        StoreMerchanEquipmentInfoVO biVo = new StoreMerchanEquipmentInfoVO();
                        biVo.storeName = "测试商户";
                        biVo.storeId = "6210000001";
                        biVo.deviceId = "6210000012";
                        biVo.appid = "wx5e5307635a345231";
                        biVo.mchid = "1490348042";
                        biVo.key = "5DD1959F432DE0F068AE8A1D84423008";
                        biVo.subAppid = "";
                        biVo.subMchid = "1528418631";
                        biVo.serviceName = "飞付科技";
                        result.code = "0000";
                        result.message = "获取成功";
                        result.data = biVo;
                }else{
                        //从0号reids库中获取信息
                        StoreMerchanEquipmentInfoVO biVo = redis.get(uuid,StoreMerchanEquipmentInfoVO.class, RedisUtils.RedisDBIndex.base);
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
                        sb.append(" CONCAT(si.id,'') AS storeId,");
                        sb.append(" si.store_name AS storeName,");
                        sb.append(" si.city AS storeCity,");
                        sb.append(" si.brand AS storeBrand,");
                        sb.append(" mi.mch_id AS subMchid,");
                        sb.append(" mi.merchan_name AS subMchName,");
                        sb.append(" mi.app_id AS subAppid,");
                        sb.append(" spi.mch_id AS mchid,");
                        sb.append(" spi.app_id AS appid,");
                        sb.append(" spi.service_name AS serviceName,");
                        sb.append(" spi.MD5_key as `key`,");
                        sb.append(" CONCAT(ei.id,'') AS deviceId");
                        sb.append(" FROM");
                        sb.append(" `store_info` si");
                        sb.append(" LEFT JOIN merchan_info mi ON si.merchant_id = mi.id");
                        sb.append(" LEFT JOIN service_provider_info spi ON spi.id = mi.service_id");
                        sb.append(" LEFT JOIN equipment_info ei ON ei.id = si.equip_id");
                        sb.append(" WHERE ei.uuid = ?");
                        try {
                                biVo = db.queryBySQL(StoreMerchanEquipmentInfoVO.class,sb.toString(),uuid);
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
                                if( StringUtil.hasText(ip)){
                                        biVo.ip = ip;
                                }
                                redis.set(uuid,biVo, RedisUtils.RedisDBIndex.base,3600);
                        }
                }
                return result;
        }

        @Override
        public Result getWxpayfaceAuthinfo(String uuid,String amount,String orderno,String ip, String rawdata) {
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
                                //生成订单信息
                                result = creatOrderInfo(uuid, amount,orderno,ip);
                                if( result != null && "0000".equals(result)){
                                        //生成订单成功
                                        response.oi = (OrderInfoPO) result.data;
                                }
                                result.code = "0000";
                                result.message = "获取人脸支付认证信息成功";
                                result.data = response;
                                return result;
                        }
                        //去微信请求
                        //准备参数
                        //获取商户信息
                        Result businessRes = getStoreMerchanInfo(uuid,ip);
                        if( businessRes == null || !"0000".equals(businessRes.code)){
                                result.code = "-111";
                                result.message = "获取人脸支付认证信息失败,请检查商户信息";
                                return result;
                        }
                        StoreMerchanEquipmentInfoVO biVO = (StoreMerchanEquipmentInfoVO)businessRes.data;
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
                        WxpayfaceAuthinfoParamDTO wxParam = new WxpayfaceAuthinfoParamDTO(biVO.storeId,biVO.storeName,biVO.deviceId,rawdata,biVO.appid,biVO.mchid,biVO.subAppid,biVO.subMchid);
                        //获取签名
                        wxParam.sign = CommonUtils.sign(wxParam,WxpayfaceAuthinfoParamDTO.class,biVO.key);
                        //调用方法
                        logger.info("签名之后 :" + wxParam.sign);
                        String res = httpUtils.sendRequest(HttpUtils.Method.POST,conf.wxpayfaceAuthinfoURL,null,XsteamUtil.toXml(wxParam,WxpayfaceAuthinfoParamDTO.class), HttpUtils.Encode.UTF8);
                        if( !StringUtil.hasText(res)){
                                result.code = "-1111";
                                result.message = "获取人脸支付认证失败,请检查网络信息";
                                return  result;
                        }
                        //将返回的xml转成对象
                        response = XsteamUtil.toBean(WxpayfaceAuthinfoResultDTO.class,res);
                        if( response != null && "SUCCESS".equals(response.returnCode)){
                                result = creatOrderInfo(uuid, amount,orderno,ip);
                                if( result != null && "0000".equals(result)){
                                        //生成订单成功
                                        response.oi = (OrderInfoPO) result.data;
                                }
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

        private Result creatOrderInfo(String uuid, String amount,String orderno, String ip) {
                Result result = new Result();
                Result businessRes = getStoreMerchanInfo(uuid,ip);
                if( businessRes == null || "0000".equals(businessRes.code)){
                        result.code = "-1111";
                        result.message = "生成订单失败,请联系开发人员";
                        return result;
                }
                StoreMerchanEquipmentInfoVO biVO = (StoreMerchanEquipmentInfoVO)businessRes.data;
                if( StringUtil.hasText(orderno) && !StringUtil.hasText(amount)){
                        //从数据库中拿
                        OrderInfoPO oi = orderDao.findByOrderno(orderno);
                        if( oi != null){
                                result.code = "0000";
                                result.message = "生成订单信息成功";
                                result.data = oi;
                                return result;
                        }else{
                                result.code = "-1111";
                                result.message = "生成订单失败,请联系开发人员";
                                return result;
                        }
                }
                orderno = String.valueOf(db.creatId(DBUtils.order_bit,biVO.storeName, DBUtils.TableIndex.order));
                OrderInfoPO oi = new OrderInfoPO();
                oi.id = db.creatId(DBUtils.id_bit,biVO.storeName, DBUtils.TableIndex.order_info);
                oi.orderno = orderno;
                oi.status = 0;
                oi.storeId = biVO.storeId;
                oi.totalAmount = Double.valueOf(amount);
                orderDao.save(oi);
                //订单详情
                OrderDetailInfoPO odi = new OrderDetailInfoPO();
                odi.id = db.creatId(DBUtils.id_bit,biVO.storeName, DBUtils.TableIndex.order_detail_info);
                odi.amount = Double.valueOf(amount);
                odi.odName = "测试商品";
                odi.orderno = orderno;
                odi.status = 0;
                odDao.save(odi);
                result.code = "0000";
                result.message = "生成订单信息成功";
                result.data = oi;
                return result;
        }

        /**
         * 初始化设备
         * @param uuid
         * @return
         */
        @Override
        @Transactional
        public Result initEquipmentInfo(String uuid,String ip) {
                Result result = new Result();
                if(!StringUtil.hasText(uuid)){
                        result.code = "-1111";
                        result.message = "UUID为空,请联系开发人员";
                        return  result;
                }
                try {
                        EquipmentInfoPO eInfo = eDao.findByUUID(uuid);
                        getStoreMerchanInfo(uuid,ip);
                        if( eInfo != null){
                                result.code = "0000";
                                result.message = "初始化设备成功,设备信息已入库";
                                result.data = eInfo;
                                return result;
                        }
                        //创建设备对象
                        eInfo = new EquipmentInfoPO();
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
        public Result setRawdata(String uuid, String rawdata, String ip) {
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
                Result res = getStoreMerchanInfo(uuid,ip);
                if( res == null || !"0000".equals(res.code)){
                        result.code = "-1111";
                        result.message = "获取人脸支付认证信息失败,请检查商户信息";
                        return result;
                }
                StoreMerchanEquipmentInfoVO biVO = (StoreMerchanEquipmentInfoVO)res.data;
                if( biVO == null){
                        result.code = "-1111";
                        result.message = "获取人脸支付认证信息失败,请检查商户信息";
                        return result;
                }
                biVO.rawdata = rawdata;
                //设置到redis
                redis.set(uuid,biVO, RedisUtils.RedisDBIndex.base,3600);
                logger.info("设置人脸支付rawdata成功");
                result.code = "0000";
                result.message = "设置成功";
                return result;
        }

        @Override
        public Result pay(String uuid, String openid, String faceCode,String orderno, String ip) {
                //参数校验
                Result result = new Result();
                //其他参数获取
                //获取门店以及商户信息
                Result storeMerchanInfo = getStoreMerchanInfo(uuid,ip);
                if( storeMerchanInfo == null || !"0000".equals(storeMerchanInfo.code)){
                        logger.error(storeMerchanInfo.message);
                        storeMerchanInfo.message = "支付失败,请检查网络";
                        return storeMerchanInfo;
                }
                StoreMerchanEquipmentInfoVO smeInfo = (StoreMerchanEquipmentInfoVO)storeMerchanInfo.data;
                //调用认证
                Result wxpayfaceAuthinfo = getWxpayfaceAuthinfo(uuid, null, orderno,ip,null);
                if( wxpayfaceAuthinfo == null || !"0000".equals(wxpayfaceAuthinfo.code)){
                        logger.error(wxpayfaceAuthinfo.message);
                        wxpayfaceAuthinfo.message = "支付失败,请检查网络";
                        return wxpayfaceAuthinfo;
                }
                WxpayfaceAuthinfoResultDTO waResult = (WxpayfaceAuthinfoResultDTO)wxpayfaceAuthinfo.data;
                //参数拼接
                WxpayfaceParamDTO paramDTO = new WxpayfaceParamDTO();
                paramDTO.appid = smeInfo.appid;
                paramDTO.subAppid = smeInfo.subAppid;
                paramDTO.mchid = smeInfo.mchid;
                paramDTO.subMchid = smeInfo.subMchid;
                paramDTO.deviceId = smeInfo.deviceId;
                paramDTO.boby = smeInfo.storeBrand + "-" + smeInfo.storeCity + smeInfo.storeName + "-测试商品";
                paramDTO.detail = "";
                paramDTO.attach = "";
                paramDTO.orderno = orderno;
                paramDTO.totalFee = String.valueOf(waResult.oi.totalAmount);
                paramDTO.spbillCreateIp = smeInfo.ip;
                paramDTO.openid = openid;
                paramDTO.faceCode = faceCode;
                //进行签名
                paramDTO.sign = CommonUtils.sign(paramDTO,WxpayfaceParamDTO.class,smeInfo.key);
                //进行请求
                String request = httpUtils.sendRequest(HttpUtils.Method.POST, conf.wxpayURL, null, XsteamUtil.toXml(paramDTO, WxpayfaceParamDTO.class), HttpUtils.Encode.UTF8);
                //转成对象
                WxpayfaceResultDTO wxpayfaceResultDTO = XsteamUtil.toBean(WxpayfaceResultDTO.class, request);
                //返回验证
                if( wxpayfaceResultDTO != null && "SUCCESS".equals(wxpayfaceResultDTO.returnCode) && "SUCCESS".equals(wxpayfaceResultDTO.resultCode)){
                        //支付成功
                        result.code = "0000";
                        result.message = "支付成功";
                }else{
                        result.code = "-1111";
                        //支付失败
                        if("FAIL".equals(wxpayfaceResultDTO.returnCode) ){
                                //通讯有问题
                                result.message = wxpayfaceResultDTO.returnMsg;
                        }else if( "FAIL".equals(wxpayfaceResultDTO.resultCode)){
                                //支付有问题
                                result.message = wxpayfaceResultDTO.errCodedes;
                        }
                }
                //返回前端
                return result;
        }

        @Override
        public Result getAuthinfo(String uuid, String amount, String ip) {
                Result result = new Result();
                String key = uuid + "_WXPAYFACE_AUTHINFO";
                WxpayfaceAuthinfoResultDTO response = redis.get(key,WxpayfaceAuthinfoResultDTO.class, RedisUtils.RedisDBIndex.base);
                if( response != null){
                        //生成订单信息
                        result = creatOrderInfo(uuid, amount,null,ip);
                        if( result != null && "0000".equals(result)){
                                //生成订单成功
                                response.oi = (OrderInfoPO) result.data;
                        }
                        result.code = "0000";
                        result.message = "获取人脸支付认证信息成功";
                        result.data = response;
                        return result;
                }
                return null;
        }
}
