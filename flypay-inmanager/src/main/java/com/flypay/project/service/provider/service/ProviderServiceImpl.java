package com.flypay.project.service.provider.service;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.common.exception.BusinessException;
import com.flypay.common.utils.StringUtils;
import com.flypay.common.utils.security.ShiroUtils;
import com.flypay.common.utils.text.Convert;
import com.flypay.framework.aspectj.lang.annotation.DataScope;
import com.flypay.project.service.merchant.domain.Merchant;
import com.flypay.project.service.merchant.service.IMerchantService;
import com.flypay.project.service.merchant.task.MerchantTaskJob;
import com.flypay.project.service.provider.domain.Provider;
import com.flypay.project.service.provider.mapper.ProviderMapper;
import com.flypay.project.service.provider.task.ProviderTaskJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 角色 业务层处理
 * 
 * @author flypay
 */
@Service
public class ProviderServiceImpl implements IProviderService
{
    @Autowired
    private ProviderMapper providerMapper;
    @Autowired
    private MerchantTaskJob merchantTaskJob;
    @Autowired
    private IMerchantService merchantService;
    @Autowired
    private ProviderTaskJob providerTaskJob;
    @Override
    @DataScope(deptAlias = "d")
    public List<Provider> selectProviderList(Provider provider) {
        return providerMapper.selectProviderList(provider);
    }

    /**
     * 修改服务商状态
     *
     * @param provider
     * @return
     */
    @Override
    public int changeStatus(Provider provider) {
        //异步的关闭服务商绑定的商户和商户绑定的门店
        if( provider != null && "1".equals(provider.getStatus())) {
            //如果是关闭则需要把与服务商相关的商户和门店全部关闭
            merchantTaskJob.changeStatus(provider.getStatus(),provider.getProviderId());
        }
        return providerMapper.updateProvider(provider);
    }

    /**
     * 根据id批量删除服务商
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteProviderByIds(String ids) {
        //解析ids
        Long[] providerIds = Convert.toLongArray(ids);
        List<Provider> ps = new ArrayList<>();
        for (Long providerId : providerIds){
            Provider provider = selectProviderById(providerId);
            if( provider == null ) {
                throw new BusinessException(String.format("%1$s找不到此服务商,不能删除", providerId));
            }
            //查看服务商是否已经绑定商户,如果绑定则无法删除
            if (countMerchantByProviderId(providerId) > 0){
                throw new BusinessException(String.format("%1$s已分配,不能删除", provider.getProviderName()));
            }
            ps.add(provider);
        }
        int i = providerMapper.deleteProviderByIds(providerIds);
        if( i >= 0){
            //更新服务商状态为停用
            providerTaskJob.closeProvideStatus(ps);
        }
        return i;
    }

    /**
     * 根据id查询商户
     * @param providerId
     * @return
     */
    @Override
    public Provider selectProviderById(Long providerId) {
        return providerMapper.selectProviderById(providerId);
    }

    /**
     * 校验服务商名称
     *
     * @param provider
     * @return
     */
    @Override
    public String checkProviderNameUnique(Provider provider) {
        Long providerId = StringUtils.isNull(provider.getProviderId()) ? -1L : provider.getProviderId();
        Provider info = providerMapper.checkProviderNameUnique(provider.getProviderName());
        if (StringUtils.isNotNull(info) && info.getProviderId().longValue() != providerId.longValue()) {
            return ServiceConstansts.PROVIDER_NAME_NOT_UNIQUE;
        }
        return ServiceConstansts.PROVIDER_NAME_UNIQUE;
    }

    /**
     * 校验服务商appid
     *
     * @param provider
     * @return
     */
    @Override
    public String checkProviderAppIdUnique(Provider provider) {
        Long providerId = StringUtils.isNull(provider.getProviderId()) ? -1L : provider.getProviderId();
        Provider info = providerMapper.checkProviderAppIdUnique(provider.getAppId());
        if (StringUtils.isNotNull(info) && info.getProviderId().longValue() != providerId.longValue()) {
            return ServiceConstansts.PROVIDER_APPID_NOT_UNIQUE;
        }
        return ServiceConstansts.PROVIDER_APPID_UNIQUE;
    }

    /**
     * 添加服务商
     *
     * @param provider
     * @return
     */
    @Override
    public int insertProvider(Provider provider) {
        provider.setCreateBy(ShiroUtils.getLoginName());
        // 新增服务商信息
        return providerMapper.insertProvider(provider);
    }

    /**
     * 更新服务商
     *
     * @param provider
     * @return
     */
    @Override
    public int updateProvider(Provider provider) {
        provider.setUpdateBy(ShiroUtils.getLoginName());
        // 修改角色信息
        return providerMapper.updateProvider(provider);
    }

    /**
     * 根据服务商id查询商户数量
     * @param providerId
     * @return
     */
    private int countMerchantByProviderId(Long providerId) {

        return merchantService.countMerchantByProviderId(providerId);
    }
}
