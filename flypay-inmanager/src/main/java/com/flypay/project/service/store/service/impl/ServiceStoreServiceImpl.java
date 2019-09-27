package com.flypay.project.service.store.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.flypay.project.service.store.mapper.ServiceStoreMapper;
import com.flypay.project.service.store.domain.ServiceStore;
import com.flypay.project.service.store.service.IServiceStoreService;
import com.flypay.common.utils.text.Convert;

/**
 * 门店Service业务层处理
 * 
 * @author flypay
 * @date 2019-09-27
 */
@Service
public class ServiceStoreServiceImpl implements IServiceStoreService 
{
    @Autowired
    private ServiceStoreMapper serviceStoreMapper;

    /**
     * 查询门店
     * 
     * @param id 门店ID
     * @return 门店
     */
    @Override
    public ServiceStore selectServiceStoreById(Long id)
    {
        return serviceStoreMapper.selectServiceStoreById(id);
    }

    /**
     * 查询门店列表
     * 
     * @param serviceStore 门店
     * @return 门店
     */
    @Override
    public List<ServiceStore> selectServiceStoreList(ServiceStore serviceStore)
    {
        return serviceStoreMapper.selectServiceStoreList(serviceStore);
    }

    /**
     * 新增门店
     * 
     * @param serviceStore 门店
     * @return 结果
     */
    @Override
    public int insertServiceStore(ServiceStore serviceStore)
    {
        return serviceStoreMapper.insertServiceStore(serviceStore);
    }

    /**
     * 修改门店
     * 
     * @param serviceStore 门店
     * @return 结果
     */
    @Override
    public int updateServiceStore(ServiceStore serviceStore)
    {
        return serviceStoreMapper.updateServiceStore(serviceStore);
    }

    /**
     * 删除门店对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteServiceStoreByIds(String ids)
    {
        return serviceStoreMapper.deleteServiceStoreByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除门店信息
     * 
     * @param id 门店ID
     * @return 结果
     */
    public int deleteServiceStoreById(Long id)
    {
        return serviceStoreMapper.deleteServiceStoreById(id);
    }
}
