package com.flypay.bp.impl;

import com.alibaba.fastjson.JSON;
import com.flypay.bp.PayService;
import com.flypay.conf.ConfigBean;
import com.flypay.model.Result;
import com.flypay.model.dao.EquipmentInfoDAO;
import com.flypay.model.dao.OrderDetailInfoDao;
import com.flypay.model.dao.OrderInfoDao;
import com.flypay.model.dto.*;
import com.flypay.model.pojo.EquipmentInfoPO;
import com.flypay.model.pojo.OrderDetailInfoPO;
import com.flypay.model.pojo.OrderInfoPO;
import com.flypay.model.vo.FacePayResult;
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
        private StoreMerchanEquipmentInfoVO getStoreMerchanInfo(String uuid, String ip) {
                Result result = new Result();
                if(!StringUtil.hasText(uuid)){
                        return  null;
                }
                StoreMerchanEquipmentInfoVO biVo = null;
                if( conf.moke){
                        //做模拟数据
                        biVo = new StoreMerchanEquipmentInfoVO();
                        biVo.storeName = "测试商户";
                        biVo.storeId = "6210000001";
                        biVo.deviceId = "6210000012";
                        biVo.appid = "wx5e5307635a345231";
                        biVo.mchid = "1490348042";
                        biVo.key = "5DD1959F432DE0F068AE8A1D84423008";
                        biVo.subAppid = "";
                        biVo.subMchid = "1528418631";
                        biVo.serviceName = "飞付科技";
                }else{
                        //从0号reids库中获取信息
                        biVo = redis.get(uuid,StoreMerchanEquipmentInfoVO.class, RedisUtils.RedisDBIndex.base);
                        if( biVo != null){
                                //获取成功
                                redis.expire(uuid, RedisUtils.RedisDBIndex.base,3600 * 24 );
                                return biVo;
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
                        sb.append(" CONCAT(ei.device_id,'') AS deviceId");
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
                                return  null;
                        }
                        if( biVo == null){
                                logger.error("该设备 :" + uuid + "无商户进行授权");
                        }else{
                                redis.set(uuid,biVo, RedisUtils.RedisDBIndex.base,3600 * 24);
                        }
                }
                return biVo;
        }

        @Override
        @Transactional
        public Result getWxpayfaceAuthinfo(String uuid, String rawdata,String orderno) {
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
                        //从redis拿
                        String key = uuid + "_AUTHINFO";
                        FacePayResult facePayResult = redis.get(key, FacePayResult.class,RedisUtils.RedisDBIndex.base);

                        if( facePayResult != null){
                                facePayResult.out_trade_no = orderno;
                                redis.set(key,facePayResult, RedisUtils.RedisDBIndex.base,Integer.valueOf(String.valueOf(redis.ttl(key,RedisUtils.RedisDBIndex.base))));
                                result.code = "0000";
                                result.message = "获取人脸支付认证信息成功";
                                result.data = facePayResult;
                                return result;
                        }
                        //去微信请求
                        //准备参数
                        //获取商户信息
                        StoreMerchanEquipmentInfoVO biVO = getStoreMerchanInfo(uuid,null);
                        if( biVO == null){
                                result.code = "-111";
                                result.message = "获取人脸支付认证信息失败,请检查商户信息";
                                return result;
                        }
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
                        if(!StringUtil.hasText(rawdata)){
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
                        String res = httpUtils.sendRequest(HttpUtils.Method.POST,conf.wxpayfaceAuthinfoURL,null,XsteamUtil.toXml(wxParam,WxpayfaceAuthinfoParamDTO.class), HttpUtils.Encode.UTF8);
                        if( !StringUtil.hasText(res)){
                                result.code = "-1111";
                                result.message = "获取人脸支付认证失败,请检查网络信息";
                                return  result;
                        }
                        //将返回的xml转成对象
                        WxpayfaceAuthinfoResultDTO response = XsteamUtil.toBean(WxpayfaceAuthinfoResultDTO.class,res);
                        if( response != null && "SUCCESS".equals(response.returnCode)){
                                //生成支付对象
                                facePayResult = new FacePayResult();
                                facePayResult.appid = biVO.appid;
                                facePayResult.authinfo = response.authinfo;
                                facePayResult.mch_id = biVO.mchid;
                                facePayResult.out_trade_no = orderno;
                                facePayResult.store_id = biVO.storeId;
                                facePayResult.sub_appid = biVO.subAppid;
                                facePayResult.sub_mch_id = biVO.subMchid;
                                facePayResult.total_fee = redis.get(orderno);
                                result.code = "0000";
                                result.message = "获取人脸支付认证信息成功";
                                //放入redis
                                Integer expiresIn = response.expiresIn;
                                key = uuid + "_AUTHINFO";
                                redis.set(key,facePayResult, RedisUtils.RedisDBIndex.base,(expiresIn - 60));
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
        public Result initEquipmentInfo(String uuid,String ip) {
                Result result = new Result();
                if(!StringUtil.hasText(uuid)){
                        result.code = "-1111";
                        result.message = "UUID为空,请联系开发人员";
                        return  result;
                }
                try {
                        EquipmentInfoPO eInfo = eDao.findByUUID(uuid);
                        if( eInfo == null){
                                eInfo = new EquipmentInfoPO();
                                eInfo.id = db.creatId(DBUtils.id_bit,null,DBUtils.TableIndex.equipment_info);
                                eInfo.uuid = uuid;
                                eInfo.type = 0;
                                eInfo = eDao.save(eInfo);
                        }else{
                                getStoreMerchanInfo(uuid,ip);
                        }
                        result.code = "0000";
                        result.message = "初始化设备成功,设备信息已入库";
                        result.data = eInfo;
                } catch (Exception e) {
                        logger.error("初始化设备失败,请联系开发人员",e);
                }
                return result;
        }

        @Override
        public Result pay(String uuid, String openid, String faceCode,String orderno) {
                //参数校验
                Result result = new Result();
                //其他参数获取
                //获取设备信息
                StoreMerchanEquipmentInfoVO storeMerchanInfo = getStoreMerchanInfo(uuid, null);
                if( storeMerchanInfo == null){
                        result.code = "-1111";
                        result.message = "系统错误,请稍后再试";
                        return result;
                }
                //参数拼接
                WxpayfaceParamDTO paramDTO = new WxpayfaceParamDTO();
                paramDTO.appid = storeMerchanInfo.appid;
                if( StringUtil.hasText(storeMerchanInfo.subAppid)){
                        paramDTO.subAppid = storeMerchanInfo.subAppid;
                }
                paramDTO.mchid = storeMerchanInfo.mchid;
                paramDTO.subMchid = storeMerchanInfo.subMchid;
                paramDTO.deviceId = storeMerchanInfo.deviceId;
                String fee = redis.get(orderno);
                paramDTO.boby = storeMerchanInfo.storeBrand + "-" + storeMerchanInfo.storeCity + storeMerchanInfo.storeName + "-测试商品";
                OrderDetailDTO orderDetail = new OrderDetailDTO("test_goods","测试商品",1,Integer.valueOf(fee));
                OrderDetailList ods = new OrderDetailList();
                ods.goods_detail.add(orderDetail) ;
                paramDTO.detail = JSON.toJSONString(ods);
                paramDTO.attach = "测试商品购买";
                paramDTO.orderno = orderno;
                paramDTO.totalFee = Integer.valueOf(fee);
                paramDTO.spbillCreateIp = storeMerchanInfo.ip;
                paramDTO.openid = openid;
                //paramDTO.goodsTag = "";
                //paramDTO.timeExpire = "";
                paramDTO.faceCode = faceCode;
                //进行签名
                paramDTO.sign = CommonUtils.sign(paramDTO,WxpayfaceParamDTO.class,storeMerchanInfo.key);
                //进行请求
                String request = httpUtils.sendRequest(HttpUtils.Method.POST, conf.wxpayURL, null, XsteamUtil.toXml(paramDTO, WxpayfaceParamDTO.class), HttpUtils.Encode.UTF8);
                logger.error("支付结果 :" + request);
                //转成对象
                WxpayfaceResultDTO wxpayfaceResultDTO = XsteamUtil.toBean(WxpayfaceResultDTO.class, request);
                //返回验证
                boolean flag = false;
                if( wxpayfaceResultDTO != null && "SUCCESS".equals(wxpayfaceResultDTO.returnCode) && "SUCCESS".equals(wxpayfaceResultDTO.resultCode)){
                        //支付成功
                        result.code = "0000";
                        result.message = "支付成功";
                        flag = true;
                }else{
                        result.code = "-1111";
                        result.message = "系统错误,请稍后再试";
                        //支付失败
                        if( wxpayfaceResultDTO != null ){
                                if("FAIL".equals(wxpayfaceResultDTO.returnCode) ){
                                        //通讯有问题
                                        result.message = wxpayfaceResultDTO.returnMsg;
                                }else if( "FAIL".equals(wxpayfaceResultDTO.resultCode)){
                                        //支付有问题
                                        result.message = wxpayfaceResultDTO.errCodedes;
                                }
                        }
                }
                updateOrder(flag,orderno,wxpayfaceResultDTO);
                //返回前端
                return result;
        }

        private void updateOrder(boolean flag, String orderno, WxpayfaceResultDTO wxpayfaceResultDTO) {
                //查询订单
                //查询订单详情
                if( flag){
                        //进行字段更新
                }else{
                        //进行字段更新
                }
        }

        @Override
        public Result creatorder(String uuid, String amount) {
                Result result = new Result();
                StoreMerchanEquipmentInfoVO biVO = getStoreMerchanInfo(uuid,null);
                if( biVO == null ){
                        result.code = "-1111";
                        result.message = "生成订单失败,请联系开发人员";
                        return result;
                }
                if( StringUtil.hasText(amount)){
                        String orderno = String.valueOf(db.creatId(DBUtils.order_bit,biVO.storeName, DBUtils.TableIndex.order));
                        //将金额放入redis
                        String fee = changeBranch(amount);
                        redis.set(orderno,fee, RedisUtils.RedisDBIndex.base,300);
                        //启动新线程
                        new Thread(() -> {
                                OrderInfoPO oi = new OrderInfoPO();
                                oi.id = db.creatId(DBUtils.id_bit,biVO.storeName, DBUtils.TableIndex.order_info);
                                oi.orderno = orderno;
                                oi.status = 0;
                                oi.storeId = biVO.storeId;
                                oi.totalAmount = Double.valueOf(amount);
                                oi.fee = fee;
                                orderDao.save(oi);
                                //订单详情
                                OrderDetailInfoPO odi = new OrderDetailInfoPO();
                                odi.id = db.creatId(DBUtils.id_bit,biVO.storeName, DBUtils.TableIndex.order_detail_info);
                                odi.amount = Double.valueOf(amount);
                                odi.odName = "测试商品";
                                odi.orderno = orderno;
                                odi.status = 0;
                                odDao.save(odi);

                        }).start();
                        result.code = "0000";
                        result.message = "生成订单信息成功";
                        result.data = orderno;
                }else{
                        result.code = "-1111";
                        result.message = "生成订单失败,请联系开发人员";
                        return result;
                }

                return result;
        }

        /**
         * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
         *
         * @param amount
         * @return
         */
        static final String REG = "\\$|\\￥|\\,";
        private static String changeBranch(String amount) {
                String currency = amount.replaceAll(REG, ""); // 处理包含, ￥
                // 或者$的金额
                int index = currency.indexOf(".");
                int length = currency.length();
                Long amLong;
                if (index == -1) {
                        amLong = Long.valueOf(currency + "00");
                } else if (length - index >= 3) {
                        amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
                } else if (length - index == 2) {
                        amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
                } else {
                        amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
                }
                return amLong.toString();
        }
}
