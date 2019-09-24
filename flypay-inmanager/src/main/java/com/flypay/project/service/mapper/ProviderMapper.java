package com.flypay.project.service.mapper;

import com.flypay.project.service.domain.Provider;
import com.flypay.project.system.role.domain.Role;

import java.util.List;

/**
 * 服务商表 数据层
 * 
 * @author flypay
 */
public interface ProviderMapper
{
    /**
     * 根据条件分页查询服务商数据
     * 
     * @param provider 服务商信息
     * @return 服务商数据集合信息
     */
    public List<Provider> selectProviderList(Provider provider);

    /**
     * 通过服务商ID查询服务商
     * 
     * @param providerId 服务商ID
     * @return 服务商对象信息
     */
    public Provider selectProviderById(Long providerId);

    /**
     * 通过服务商ID删除服务商
     * 
     * @param providerId 服务商ID
     * @return 结果
     */
    public int deleteProviderById(Long providerId);

    /**
     * 批量服务商用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProviderByIds(Long[] ids);

    /**
     * 修改服务商信息
     * 
     * @param provider 服务商信息
     * @return 结果
     */
    public int updateProvider(Provider provider);

    /**
     * 新增服务商信息
     * 
     * @param provider 服务商信息
     * @return 结果
     */
    public int insertProvider(Provider provider);

    /**
     * 校验服务商名称是否唯一
     * 
     * @param providerName 服务商名称
     * @return 服务商信息
     */
    public Provider checkProviderNameUnique(String providerName);
    
    /**
     * 校验服务商appid是否唯一
     * 
     * @param appId 服务商appid
     * @return 服务商信息
     */
    public Provider checkProviderAppIdUnique(String appId);
}
