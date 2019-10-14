package com.flypay.framework.web.service;

import com.flypay.project.service.merchant.domain.Merchant;
import com.flypay.project.service.merchant.service.IMerchantService;
import com.flypay.project.service.merchant.vo.MerchantVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("merchant")
public class MerchantService {
    @Autowired
    private IMerchantService merchantService;

    public List<MerchantVo> searchAllMerchant(){
        Merchant merchant = new Merchant();
        merchant.setStatus("0");
        List<Merchant> ms = merchantService.selectMerchantList(merchant);
        List<MerchantVo> mvs = null;
        if( ms != null && !ms.isEmpty()){
            mvs = new ArrayList<>();
            for(Merchant m : ms){
                MerchantVo mv = new MerchantVo();
                mv.merchantId = m.getMerchantId();
                mv.merchantName = m.getMerchantName();
                mvs.add(mv);
            }
        }
        return mvs;
    }
}
