package com.flypay.framework.web.service;

import com.flypay.project.service.provider.domain.Provider;
import com.flypay.project.service.provider.vo.ProviderVo;
import com.flypay.project.service.provider.service.IProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("providerservice")
public class ProviderService {
    @Autowired
    IProviderService providerService;

    public List<ProviderVo> searchALLProviders(){
        Provider provider = new Provider();
        provider.setStatus("0");
        List<Provider> ps = providerService.selectProviderList(provider);
        //查询所有服务商列表
        List<ProviderVo> pvs = null;
        if( ps != null && !ps.isEmpty()){
            pvs = new ArrayList<>();
            //遍历并且取出所有服务商
            for (Provider p : ps) {
                ProviderVo pv = new ProviderVo();
                pv.providerId = p.getProviderId();
                pv.providerName = p.getProviderName();
                pvs.add(pv);
            }
        }
        return pvs;
    }
}
