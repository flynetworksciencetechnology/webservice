package com.flypay.project.service.merchant.task;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.project.service.merchant.domain.Merchant;
import com.flypay.project.service.merchant.mapper.MerchantMapper;
import com.flypay.project.service.merchant.service.IMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用来执行异步方法的
 */
@Component
public class MerchantTaskJob {
    @Autowired
    private MerchantMapper merchantMapper;
    /**
     * 修改服务商状态
     * @param status
     * @param providerId
     * @return
     */
    @Async
    public int changeStatus(String status,Long providerId){
        Merchant merchant = new Merchant();
        merchant.setStatus(status);
        merchant.setProviderId(providerId);
        return merchantMapper.updateMerchant(merchant);
    }

    @Async
    public void closeMerchantStatus(List<Merchant> ms) {
        if( ms != null && !ms.isEmpty()){
            for ( Merchant m : ms) {
                m.setStatus(ServiceConstansts.STOP_STATUS);
                merchantMapper.updateMerchant(m);
            }
        }
    }
}
