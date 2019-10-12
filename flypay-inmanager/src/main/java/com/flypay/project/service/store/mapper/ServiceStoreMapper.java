package com.flypay.project.service.store.mapper;

import com.flypay.project.service.store.domain.ServiceStore;
import java.util.List;

/**
 * 门店Mapper接口
 * 
 * @author flypay
 * @date 2019-09-27
 */
public interface ServiceStoreMapper 
{
    /**
     * 查询门店
     * 
     * @param id 门店ID
     * @return 门店
     */
    public ServiceStore selectServiceStoreById(Long id);

    /**
     * 查询门店列表
     * 
     * @param serviceStore 门店
     * @return 门店集合
     */
    public List<ServiceStore> selectServiceStoreList(ServiceStore serviceStore);

    /**
     * 新增门店
     * 
     * @param serviceStore 门店
     * @return 结果
     */
    public int insertServiceStore(ServiceStore serviceStore);

    /**
     * 修改门店
     * 
     * @param serviceStore 门店
     * @return 结果
     */
    public int updateServiceStore(ServiceStore serviceStore);

    /**
     * 删除门店
     * 
     * @param id 门店ID
     * @return 结果
     */
    public int deleteServiceStoreById(Long id);

    /**
     * 批量删除门店
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteServiceStoreByIds(Long[] ids);


    /**
     * 修改门店状态
     * @param providerId
     * @return
     */
    int changeStatus(Long providerId);

}
