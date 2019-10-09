package com.flypay.project.service.equipment.mapper;

import com.flypay.project.service.equipment.domain.Equipment;

import java.util.List;

/**
 * 设备表 数据层
 * 
 * @author flypay
 */
public interface EquipmentMapper
{
    /**
     * 根据条件分页查询设备数据
     * 
     * @param equipment 设备信息
     * @return 设备数据集合信息
     */
    public List<Equipment> selectEquipmentList(Equipment equipment);

    /**
     * 通过设备ID查询设备
     * 
     * @param equipmentId 设备ID
     * @return 设备对象信息
     */
    public Equipment selectEquipmentById(Long equipmentId);

    /**
     * 通过设备ID删除设备
     * 
     * @param equipmentId 设备ID
     * @return 结果
     */
    public int deleteEquipmentById(Long equipmentId);

    /**
     * 批量删除设备
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteEquipmentByIds(Long[] ids);

    /**
     * 修改设备信息
     * 
     * @param equipment 设备信息
     * @return 结果
     */
    public int updateEquipment(Equipment equipment);

    /**
     * 新增设备信息
     * 
     * @param equipment 设备信息
     * @return 结果
     */
    public int insertEquipment(Equipment equipment);

    /**
     * 校验设备名称是否唯一
     * 
     * @param deviceId 设备名称
     * @return 设备信息
     */
    public Equipment checkEquipmentDeviceIdUnique(String deviceId);

    List<Equipment> selectEquipmentListByMerchantId(Long merchantId);
}
