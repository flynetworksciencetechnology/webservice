package com.flypay.project.service.store.service.impl;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.common.utils.redis.RedisUtils;
import com.flypay.framework.web.service.DictService;
import com.flypay.project.service.store.service.StoreInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class StoreInterfaceImpl implements StoreInterface {

    @Autowired
    RedisUtils redis;
    @Autowired
    DictService dict;
    /**
     * 根据服务商获取正在运行设备的个数
     * @param providerId
     * @param merchantId
     * @param storeId
     * @return
     */
    @Override
    public Integer getRunningEquipmentCount(Long providerId,Long merchantId,Long storeId,Long equipmentId) {
        //校验参数
        String key = getKey(providerId, merchantId, storeId,equipmentId,ServiceConstansts.EQUIPMENT_REDIS_TYPE.RUN_TYPE.name());
        //使用redis获取所有现有条件所有运行设备的运行状态
        List<String> values = redis.getByKeys(key, RedisUtils.RedisDBIndex.base);
        //无运行设备
        if( values == null || values.isEmpty()){
            return 0;
        }
        //有运行设备
        String code = dict.getValue(ServiceConstansts.EQUIPMENT_RUN_STATUS, ServiceConstansts.EQUIPMENT_RUN_NAME.运行中.name());
        return get(values,code);
    }

    private String getKey(Long providerId, Long merchantId, Long storeId,Long equipmentId,String type) {
        String key = type + ServiceConstansts.REDIS_SEPARATOR;
        if( providerId != null && providerId != 0){
            key += providerId + ServiceConstansts.REDIS_SEPARATOR;
        }else{
            key += ServiceConstansts.REDIS_WILDCARD + ServiceConstansts.REDIS_SEPARATOR;
        }
        if( merchantId != null && merchantId != 0){
            key += merchantId + ServiceConstansts.REDIS_SEPARATOR;
        }else{
            key += ServiceConstansts.REDIS_WILDCARD + ServiceConstansts.REDIS_SEPARATOR;
        }
        if( storeId != null && storeId != 0){
            key += storeId + ServiceConstansts.REDIS_SEPARATOR;
        }else{
            key += ServiceConstansts.REDIS_WILDCARD + ServiceConstansts.REDIS_SEPARATOR;
        }
        if( equipmentId != null && equipmentId != 0){
            key += equipmentId + ServiceConstansts.REDIS_SEPARATOR;
        }else{
            key += ServiceConstansts.REDIS_WILDCARD + ServiceConstansts.REDIS_SEPARATOR;
        }
        key += key + ServiceConstansts.REDIS_WILDCARD;
        return key;
    }

    /**
     * 关闭运行中的设备
     *
     * @param providerId
     * @param merchantId
     * @param storeId
     * @return
     */
    @Override
    public Integer closeEquipment(Long providerId, Long merchantId, Long storeId,Long equipmentId) {
        //运行状态key
        String run_key = getKey(providerId, merchantId, storeId,equipmentId,ServiceConstansts.EQUIPMENT_REDIS_TYPE.RUN_TYPE.name());
        //设备信息key
        String info_key  = getKey(providerId, merchantId, storeId,equipmentId,ServiceConstansts.EQUIPMENT_REDIS_TYPE.INFO.name());
        //获取所有key
        Set<String> run_keys = redis.getKeys(run_key, null);
        Set<String> info_keys = redis.getKeys(info_key, null);
        //给虽所有key设置值
        String code = dict.getValue(ServiceConstansts.EQUIPMENT_RUN_STATUS, ServiceConstansts.EQUIPMENT_RUN_NAME.停止运行.name());
        if( run_keys != null && !run_keys.isEmpty()){
            for (String k : run_keys) {
                redis.set(k,code,null);
            }
        }
        if( info_keys != null && !info_keys.isEmpty()){
            for (String k : info_keys) {
                redis.delete(k);
            }
        }
        return 1;
    }

    /**
     * 启动指定设备
     *
     * @param providerId
     * @param merchantId
     * @param storeId
     * @param equipmentId
     * @return
     */
    @Override
    public Integer openEquipment(Long providerId, Long merchantId, Long storeId, Long equipmentId) {
        String run_key = getKey(providerId, merchantId, storeId,equipmentId,ServiceConstansts.EQUIPMENT_REDIS_TYPE.RUN_TYPE.name());
        Set<String> run_keys = redis.getKeys(run_key, null);
        //给虽所有key设置值
        String code = dict.getValue(ServiceConstansts.EQUIPMENT_RUN_STATUS, ServiceConstansts.EQUIPMENT_RUN_NAME.闲置.name());
        if( run_keys != null && !run_keys.isEmpty()){
            for (String k : run_keys) {
                redis.set(k,code,null);
            }
        }
        return 1;
    }

    private int get(List<String> list, String code) {
        int i = 0;
        for (String s : list) {
            if (s.equals(code)) {
                i ++;
            }
        }
        return i;
    }
}
