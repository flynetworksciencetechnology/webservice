package com.flypay.project.service.merchant.mapper;

import com.flypay.project.service.merchant.domain.Merchant;

import java.util.List;

/**
 * 商户表 数据层
 * 
 * @author flypay
 */
public interface MerchantMapper
{
    /**
     * 根据条件分页查询商户数据
     * 
     * @param merchant 商户信息
     * @return 商户数据集合信息
     */
    List<Merchant> selectMerchantList(Merchant merchant);

    /**
     * 通过商户ID查询商户
     * 
     * @param merchantId 商户ID
     * @return 商户对象信息
     */
    Merchant selectMerchantById(Long merchantId);


    /**
     * 批量商户用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteMerchantByIds(Long[] ids);

    /**
     * 修改商户信息
     * 
     * @param merchant 商户信息
     * @return 结果
     */
    int updateMerchant(Merchant merchant);

    /**
     * 新增商户信息
     * 
     * @param merchant 商户信息
     * @return 结果
     */
    int insertMerchant(Merchant merchant);

    /**
     * 校验商户名称是否唯一
     * 
     * @param merchantName 商户名称
     * @return 商户信息
     */
    Merchant checkMerchantNameUnique(String merchantName);
    
    /**
     * 校验商户appid是否唯一
     * 
     * @param appId 商户appid
     * @return 商户信息
     */
    Merchant checkMerchantAppIdUnique(String appId);

    /**
     * 根据服务商id查询商户个数未删除
     * @param providerId
     * @return
     */
    int countMerchantByProviderId(Long providerId);
}
