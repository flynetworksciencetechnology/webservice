package com.flypay.project.service.equipment.service;

import com.flypay.project.service.equipment.domain.Equipment;

import java.util.List;

/**
 * 角色业务层
 * 
 * @author flypay
 */
public interface IEquipmentService
{
    /**
     * 根据条件分页查询设备数据
     * 
     * @param equipment 设备信息
     * @return 设备数据集合信息
     */
    public List<Equipment> selectEquipmentList(Equipment equipment);

    /**
     * 修改设备状态
     * @param equipment
     * @return
     */
    int changeStatus(Equipment equipment);
    /**
     * 批量修改设备状态
     * @param eIds
     * @return
     */
    int changeStatus(List<Long> eIds,String status);

    /**
     * 根据id批量删除设备
     * @param ids
     * @return
     */
    int deleteEquipmentByIds(String ids);

    /**
     * 根据id查询设备
     * @param equipmentId
     * @return
     */
    Equipment selectEquipmentById(Long equipmentId);

    /**
     * 校验设备名称
     * @param equipment
     * @return
     */
    String checkEquipmentDeviceIdUnique(Equipment equipment);


    /**
     * 添加设备
     * @param equipment
     * @return
     */
    int insertEquipment(Equipment equipment);

    /**
     * 更新设备
     * @param equipment
     * @return
     */
    int updateEquipment(Equipment equipment);

}
