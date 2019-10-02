package com.flypay.project.service.store.service.impl;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.common.utils.redis.RedisUtils;
import com.flypay.framework.web.service.DictService;
import com.flypay.project.service.store.service.StoreInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
     * @param StoreId
     * @return
     */
    @Override
    public Integer getRunningEquipmentCount(Long providerId,Long merchantId,Long storeId) {
        //校验参数
        String key = "";
        if( providerId != null && providerId != 0){
            key += providerId + ServiceConstansts.REDIS_SEPARATOR;
        }
        if( merchantId != null && merchantId != 0){
            key += merchantId + ServiceConstansts.REDIS_SEPARATOR;
        }
        if( storeId != null && storeId != 0){
            key += storeId + ServiceConstansts.REDIS_SEPARATOR;
        }
        key += key + ServiceConstansts.REDIS_WILDCARD;
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
