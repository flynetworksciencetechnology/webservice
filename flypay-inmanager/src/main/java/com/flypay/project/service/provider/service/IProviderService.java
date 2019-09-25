package com.flypay.project.service.provider.service;

import com.flypay.project.service.provider.domain.Provider;
import com.flypay.project.service.provider.vo.ProviderVo;

import java.util.List;

/**
 * 角色业务层
 * 
 * @author flypay
 */
public interface IProviderService
{
    /**
     * 根据条件分页查询服务商数据
     * 
     * @param provider 服务商信息
     * @return 服务商数据集合信息
     */
    public List<Provider> selectProviderList(Provider provider);

    /**
     * 修改服务商状态
     * @param provider
     * @return
     */
    int changeStatus(Provider provider);

    /**
     * 根据id批量删除服务商
     * @param ids
     * @return
     */
    int deleteProviderByIds(String ids);

    /**
     * 根据id查询服务商
     * @param providerId
     * @return
     */
    Provider selectProviderById(Long providerId);

    /**
     * 校验服务商名称
     * @param provider
     * @return
     */
    String checkProviderNameUnique(Provider provider);

    /**
     * 校验服务商appid
     * @param provider
     * @return
     */
    String checkProviderAppIdUnique(Provider provider);

    /**
     * 添加服务商
     * @param provider
     * @return
     */
    int insertProvider(Provider provider);

    /**
     * 更新服务商
     * @param provider
     * @return
     */
    int updateProvider(Provider provider);

}
