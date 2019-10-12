package com.flypay.project.service.store.task;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.project.service.store.domain.ServiceStore;
import com.flypay.project.service.store.mapper.ServiceStoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreTaskJob {

    @Autowired
    ServiceStoreMapper storeMapper;
    @Async
    public void closeStoreStatus(List<ServiceStore> ss){
        if( ss != null && !ss.isEmpty()){
            for (ServiceStore s : ss) {
                s.setStatus(ServiceConstansts.STOP_STATUS);
                storeMapper.updateServiceStore(s);
            }
        }
    }
}
