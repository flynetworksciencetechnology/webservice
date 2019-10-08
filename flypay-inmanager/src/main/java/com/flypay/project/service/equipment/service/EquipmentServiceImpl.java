package com.flypay.project.service.equipment.service;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.common.exception.BusinessException;
import com.flypay.common.utils.StringUtils;
import com.flypay.common.utils.security.ShiroUtils;
import com.flypay.common.utils.text.Convert;
import com.flypay.framework.aspectj.lang.annotation.DataScope;
import com.flypay.project.service.equipment.domain.Equipment;
import com.flypay.project.service.equipment.mapper.EquipmentMapper;
import com.flypay.project.service.equipment.task.EquipmentTaskJob;
import com.flypay.project.service.merchant.service.IMerchantService;
import com.flypay.project.service.merchant.task.MerchantTaskJob;
import com.flypay.project.service.store.service.StoreInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色 业务层处理
 * 
 * @author flypay
 */
@Service
public class EquipmentServiceImpl implements IEquipmentService
{
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private EquipmentTaskJob equipmentTaskJob;
    @Autowired
    private StoreInterface storeInterface;
    @Override
    @DataScope(deptAlias = "d")
    public List<Equipment> selectEquipmentList(Equipment equipment) {
        return equipmentMapper.selectEquipmentList(equipment);
    }

    /**
     * 修改设备状态
     *
     * @param equipment
     * @return
     */
    @Override
    public int changeStatus(Equipment equipment) {
        //同步删除正在使用中的设备缓存
        if( equipment != null && "1".equals(equipment.getStatus())) {
            Integer count = storeInterface.getRunningEquipmentCount(null,null,null,equipment.getEquipmentId());
            if( !Integer.valueOf(0).equals(count)){
                throw new BusinessException(equipment.getEquipmentId() + "该设备正在运行,无法停用");
            }
            //关闭所有设备
            storeInterface.closeEquipment(null, null, null,equipment.getEquipmentId());
            //判断设备的运行状态,如果是闲置则可以直接删除缓存如果非闲置则不可停用设备
            //equipmentTaskJob.changeStatus(equipment.getStatus(),equipment.getEquipmentId());
        }else{
            //如果是启动
            //则将本设备
        }
        return equipmentMapper.updateEquipment(equipment);
    }

    /**
     * 修改设备状态
     *
     * @param eIds
     * @return
     */
    @Override
    public int changeStatus(List<Long> eIds, String status) {

        if( eIds == null || eIds.isEmpty()){
            //可以关闭
            return 0;
        }
        if( status != null && "1".equals(status)) {
            //设备是否在运行
            for (Long eId:eIds) {
                Integer count = storeInterface.getRunningEquipmentCount(null,null,null,eId);
                if( !Integer.valueOf(0).equals(count)){
                    throw new BusinessException(eId + "该设备正在运行,无法停用");
                }
            }
        }
        //关闭设备
        int i = 0;
        for (Long eId:eIds) {
            if( status != null && "1".equals(status)){
                storeInterface.closeEquipment(null, null, null,eId);
            }
            Equipment equipment = new Equipment();
            equipment.setEquipmentId(eId);
            equipment.setStatus(status);
            changeStatus(equipment);
            i++;
        }

        return  i;
    }

    /**
     * 根据id批量删除设备
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteEquipmentByIds(String ids) {
        //解析ids
        Long[] equipmentIds = Convert.toLongArray(ids);
        for (Long equipmentId : equipmentIds){
            Equipment equipment = selectEquipmentById(equipmentId);
            if( equipment == null ) {
                throw new BusinessException(String.format("%1$s找不到此设备,不能删除", equipmentId));
            }
            //查看设备是否已经绑定商户,如果绑定则无法删除
            if ("1".equals(equipment.getIsBand())){
                throw new BusinessException(String.format("%1$s已绑定,不能删除", equipment.getDeviceId()));
            }
        }
        return equipmentMapper.deleteEquipmentByIds(equipmentIds);
    }

    /**
     * 根据id查询商户
     * @param equipmentId
     * @return
     */
    @Override
    public Equipment selectEquipmentById(Long equipmentId) {
        return equipmentMapper.selectEquipmentById(equipmentId);
    }

    /**
     * 校验设备名称
     *
     * @param equipment
     * @return
     */
    @Override
    public String checkEquipmentDeviceIdUnique(Equipment equipment) {
        Long equipmentId = StringUtils.isNull(equipment.getEquipmentId()) ? -1L : equipment.getEquipmentId();
        Equipment info = equipmentMapper.checkEquipmentDeviceIdUnique(equipment.getDeviceId());
        if (StringUtils.isNotNull(info) && info.getEquipmentId().longValue() != equipmentId.longValue()) {
            return ServiceConstansts.PROVIDER_NAME_NOT_UNIQUE;
        }
        return ServiceConstansts.PROVIDER_NAME_UNIQUE;
    }

    
    /**
     * 添加设备
     *
     * @param equipment
     * @return
     */
    @Override
    public int insertEquipment(Equipment equipment) {
        equipment.setCreateBy(ShiroUtils.getLoginName());
        // 新增设备信息
        return equipmentMapper.insertEquipment(equipment);
    }

    /**
     * 更新设备
     *
     * @param equipment
     * @return
     */
    @Override
    public int updateEquipment(Equipment equipment) {
        equipment.setUpdateBy(ShiroUtils.getLoginName());
        // 修改角色信息
        return equipmentMapper.updateEquipment(equipment);
    }

}
