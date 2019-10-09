package com.flypay.project.service.store.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.common.exception.BusinessException;
import com.flypay.project.service.equipment.domain.Equipment;
import com.flypay.project.service.equipment.service.IEquipmentService;
import com.flypay.project.service.merchant.domain.Merchant;
import com.flypay.project.service.merchant.service.IMerchantService;
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

    @Autowired
    private IMerchantService merchantService;
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
    public int deleteServiceStoreByIds(String ids){
        //
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
    public int changeStatus(ServiceStore store) {

        List<Long> eids = null;
        if( ServiceConstansts.STOP_STATUS.equals(store.getStatus())){
            if( store.getProviderId() != null){
                //查询服务商是否有运行中的设备
                Integer count = storeInterface.getRunningEquipmentCount(store.getProviderId(),null,null,null);
                if( !Integer.valueOf(0).equals(count)){
                    throw new BusinessException("该服务商有正在运行的设备,无法停用");
                }
                //查询服务商下所有的设备id
                Equipment equipment = new Equipment();
                equipment.setProviderId(store.getProviderId());
                equipment.setStatus(ServiceConstansts.USING_STATUS);//启用状态的
                List<Equipment> es = equipmentService.selectEquipmentList(equipment);
                if( es != null){
                    eids = new ArrayList<>();
                    for (Equipment e :es) {
                        eids.add(e.getEquipmentId());
                    }
                }
            }else if( store != null && store.getMerchantId() != null && store.getStoreId() == null){
                //查询商户下运行中的设备
                Integer count = storeInterface.getRunningEquipmentCount(null,store.getMerchantId(),null,null);
                if( !Integer.valueOf(0).equals(count)){
                    throw new BusinessException("该商户下有正在运行的设备,无法停用");
                }
                //关闭商户下的所有设备
                //查询商户下所有的设备id
                List<Equipment> es = equipmentService.selectEquipmentListByMerchantId(store.getMerchantId());
                if( es != null){
                    eids = new ArrayList<>();
                    for (Equipment e :es) {
                        eids.add(e.getEquipmentId());
                    }
                }
            }else if( store.getStoreId() != null){
                //查询门店下运行中的设备
                Integer count = storeInterface.getRunningEquipmentCount(null,null,store.getStoreId(),null);
                if( !Integer.valueOf(0).equals(count)){
                    throw new BusinessException("该门店下有正在运行的设备,无法停用");
                }
                //查询该门店下的设备id
                ServiceStore info = serviceStoreMapper.selectServiceStoreById(store.getStoreId());
                eids = new ArrayList<>();
                eids.add(info.getEquipmentId());
            }
            //如果都没有运行中的则关闭所有设备
            equipmentService.changeStatus(eids,store.getStatus());
        }else{
            //查询服务商状态
            Merchant merchant = merchantService.selectMerchantByStoreId(store.getStoreId());
            if( merchant == null){
                //未绑定商户
                throw new BusinessException("错误:门店未绑定商户,无法启用");
            }
            if( ServiceConstansts.STOP_STATUS.equals(merchant.getStatus())){
                throw new BusinessException("错误:所属商户已经停用,无法启用");
            }
            //则将本设备改成闲置
            storeInterface.openEquipment(null, null, store.getStoreId(),null);
            //将门店下的设备置为闲置
        }

        return serviceStoreMapper.updateServiceStore(store);
    }

    /**
     * 根据设备查询门店
     * @param equipmentId
     * @return
     */
    @Override
    public ServiceStore selectServiceStoreByEquipmentId(Long equipmentId) {
        ServiceStore store = new ServiceStore();
        store.setEquipmentId(equipmentId);
        List<ServiceStore> ss = serviceStoreMapper.selectServiceStoreList(store);
        if( ss == null || ss.isEmpty()){
            return null;
        }
        return ss.get(0);
    }

    /**
     * 根据条件查询商户数量
     *
     * @param store
     * @return
     */
    @Override
    public int countStoreByMerchantId(ServiceStore store) {
        List<ServiceStore> ss = serviceStoreMapper.selectServiceStoreList(store);
        if( ss == null || ss.isEmpty()){
            return 0;
        }
        return ss.size();
    }


}