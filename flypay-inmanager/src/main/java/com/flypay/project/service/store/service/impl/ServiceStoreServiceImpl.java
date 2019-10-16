package com.flypay.project.service.store.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.common.exception.BusinessException;
import com.flypay.common.utils.StringUtils;
import com.flypay.common.utils.security.ShiroUtils;
import com.flypay.project.service.equipment.domain.Equipment;
import com.flypay.project.service.equipment.service.IEquipmentService;
import com.flypay.project.service.merchant.domain.Merchant;
import com.flypay.project.service.merchant.service.IMerchantService;
import com.flypay.project.service.store.service.StoreInterface;
import com.flypay.project.service.store.task.StoreTaskJob;
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
    @Autowired
    private StoreTaskJob storeTaskJob;
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
    public int insertServiceStore(ServiceStore serviceStore){
        //拿出设备,查询设备信息
        if( serviceStore.getEquipmentId() == null || "".equals(serviceStore.getEquipmentId())){
            throw new BusinessException("错误:请选择需要绑定的设备");
        }
        Equipment e = equipmentService.selectEquipmentById(serviceStore.getEquipmentId());
        //检查设备是否可用,不必校验设备的状态,校验设备的信息即可,是否绑定
        if( e == null){
            throw new BusinessException("错误:所选设备不存在");
        }
        //如果不可用则报错
        if( !equipmentService.isCanRun(e)){
            throw new BusinessException("错误:所选设备信息异常,请联系管理员解决!!!");
        }
        //如果可用则将设备状态置为启用.并且修改设备的绑定信息
        e.setIsBand("1");
        e.setStatus("0");
        equipmentService.updateEquipment(e);
        storeInterface.openEquipment(e.getProviderId(),serviceStore.getMerchantId(),null,e.getEquipmentId());
        serviceStore.setCreateBy(ShiroUtils.getLoginName());
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
        //解析ids
        Long[] storeIds = Convert.toLongArray(ids);
        List<ServiceStore> ms = new ArrayList<>();
        for (Long storeId : storeIds){
            ServiceStore store = selectServiceStoreById(storeId);
            if( store == null ) {
                throw new BusinessException(String.format("错误:%1$s找不到此门店,不能删除", storeId));
            }
            //查看商戶是否已經綁定門店,如果綁定則無法刪除
            if (store.getEquipmentId() != null){
                throw new BusinessException(String.format("错误:%1$s:该门店已经绑定设备,不能删除", store.getStoreName()));
            }
            ms.add(store);
        }
        int i = serviceStoreMapper.deleteServiceStoreByIds(storeIds);
        if( i >= 0){
            //将删除的商户状态改成关闭
            storeTaskJob.closeStoreStatus(ms);
        }
        return i;
    }



    /**
     * 删除门店信息
     * 
     * @param id 门店ID
     * @return 结果
     */
    public int deleteServiceStoreById(Long id){
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
                Equipment equipment = new Equipment();
                equipment.setStatus("0");
                equipment.setIsBand("1");
                equipment.setProviderId(store.getMerchantId());
                List<Equipment> es = equipmentService.selectEquipmentListByMerchantId(equipment);
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

    /**
     * 校验门店名字是否存在
     *
     * @param store
     * @return
     */
    @Override
    public String checkStoreNameUnique(ServiceStore store) {
        Long storeId = StringUtils.isNull(store.getStoreId()) ? -1L : store.getStoreId();
        ServiceStore info = serviceStoreMapper.checkStoreNameUnique(store.getStoreName());
        if (StringUtils.isNotNull(info) && info.getMerchantId().longValue() != storeId.longValue()) {
            return ServiceConstansts.STORE_NAME_NOT_UNIQUE;
        }
        return ServiceConstansts.STORE_NAME_UNIQUE;
    }


}
