package com.flypay.model.dao;

import com.flypay.model.dao.base.BaseDao;
import com.flypay.model.pojo.EquipmentInfoPO;

public interface EquipmentInfoDAO extends BaseDao<EquipmentInfoPO> {

    EquipmentInfoPO save(EquipmentInfoPO eInfo);
}
