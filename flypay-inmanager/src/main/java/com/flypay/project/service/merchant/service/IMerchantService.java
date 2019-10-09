package com.flypay.project.service.merchant.service;

import com.flypay.project.service.merchant.domain.Merchant;
import com.flypay.project.service.provider.vo.ProviderVo;

import java.util.List;

/**
 * 角色业务层
 * 
 * @author flypay
 */
public interface IMerchantService
{
    /**
     * 根据条件分页查询服务商数据
     * 
     * @param merchant 服务商信息
     * @return 服务商数据集合信息
     */
    public List<Merchant> selectMerchantList(Merchant merchant);

    /**
     * 修改服务商状态
     * @param merchant
     * @return
     */
    int changeStatus(Merchant merchant);

    /**
     * 根据id批量删除服务商
     * @param ids
     * @return
     */
    int deleteMerchantByIds(String ids);

    /**
     * 根据id查询服务商
     * @param merchantId
     * @return
     */
    Merchant selectMerchantById(Long merchantId);

    /**
     * 校验服务商名称
     * @param merchant
     * @return
     */
    String checkMerchantNameUnique(Merchant merchant);

    /**
     * 校验服务商appid
     * @param merchant
     * @return
     */
    String checkMerchantAppIdUnique(Merchant merchant);

    /**
     * 添加服务商
     * @param merchant
     * @return
     */
    int insertMerchant(Merchant merchant);

    /**
     * 更新服务商
     * @param merchant
     * @return
     */
    int updateMerchant(Merchant merchant);

    /**
     * 根据服务商id查询是否有下属商户,必须是非删除状态的
     * @param providerId
     * @return
     */
    int countMerchantByProviderId(Long providerId);

    /**
     * 根据门店id查询商户信息
     * @param storeId
     * @return
     */
    Merchant selectMerchantByStoreId(Long storeId);
}
