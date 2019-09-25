package com.flypay.project.service.provider.task;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.project.service.provider.domain.Provider;
import com.flypay.project.service.provider.mapper.ProviderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProviderTaskJob {

    @Autowired
    ProviderMapper providerMapper;
    @Async
    public void closeProvideStatus(List<Provider> ps){
        if( ps != null && !ps.isEmpty()){
            for (Provider p : ps) {
                p.setStatus(ServiceConstansts.STOP_STATUS);
                providerMapper.updateProvider(p);
            }
        }
    }
}
