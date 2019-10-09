package com.flypay.project.service.store.service;

/**
 * 门店服务接口
 */
public interface StoreInterface {

    /**
     * 根据服务商获取正在运行设备的个数
     * @param providerId
     * @param merchantId
     * @param StoreId
     * @return
     */
    Integer getRunningEquipmentCount(Long providerId, Long merchantId, Long StoreId,Long equipmentId);
    /**
     * 关闭运行中的设备
     * @param providerId
     * @param merchantId
     * @param storeId
     * @param equipmentId
     * @return
     */
    Integer closeEquipment(Long providerId, Long merchantId, Long storeId,Long equipmentId);

    /**
     * 启动指定设备
     * @param providerId
     * @param merchantId
     * @param storeId
     * @param equipmentId
     * @return
     */
    Integer openEquipment(Long providerId, Long merchantId, Long storeId, Long equipmentId);
}
