package com.flypay.project.service.store.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.common.exception.BusinessException;
import com.flypay.project.service.equipment.domain.Equipment;
import com.flypay.project.service.equipment.service.IEquipmentService;
import com.flypay.project.service.store.service.StoreInterface;
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

    @Autowired
    private IEquipmentService equipmentService;
    @Autowired
    private StoreInterface storeInterface;

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

    /**
     * 修改门店状态
     *
     * @param store
     */
    @Override
    public int changeStatus(ServiceStore store,Long providerId) {

        List<Long> eids = null;
        if( providerId != null){
            //查询服务商是否有运行中的设备
            Integer count = storeInterface.getRunningEquipmentCount(providerId,null,null,null);
            if( !Integer.valueOf(0).equals(count)){
                throw new BusinessException("该服务商有正在运行的设备,无法停用");
            }
            //查询服务商下所有的设备id
            Equipment equipment = new Equipment();
            equipment.setProviderId(providerId);
            equipment.setStatus(ServiceConstansts.USING_STATUS);//启用状态的
            List<Equipment> es = equipmentService.selectEquipmentList(equipment);
            if( es != null){
                eids = new ArrayList<>();
                for (Equipment e :es) {
                    eids.add(e.getEquipmentId());
                }
            }
        }else if( store != null && store.getMerchantId() != null && store.getId() == null){
            //查询商户下运行中的设备
            Integer count = storeInterface.getRunningEquipmentCount(null,store.getMerchantId(),null,null);
            if( !Integer.valueOf(0).equals(count)){
                throw new BusinessException("该商户下有正在运行的设备,无法停用");
            }
            //关闭商户下的所有设备
            //查询商户下所有的设备id
        }else if( store.getId() != null){
            //查询门店下运行中的设备
            Integer count = storeInterface.getRunningEquipmentCount(null,null,store.getId(),null);
            if( !Integer.valueOf(0).equals(count)){
                throw new BusinessException("该门店下有正在运行的设备,无法停用");
            }
            //查询该门店下的设备id
            ServiceStore info = serviceStoreMapper.selectServiceStoreById(store.getId());
            eids = new ArrayList<>();
            eids.add(info.getEquipmentId());
        }
        //如果都没有运行中的则关闭所有设备
        equipmentService.changeStatus(eids,store.getStatus());
        return serviceStoreMapper.updateServiceStore(store);
    }


}
