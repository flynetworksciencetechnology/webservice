package com.flypay.model.dao.impl;

import com.flypay.model.dao.EquipmentInfoDAO;
import com.flypay.model.dao.base.AbstractBaseDao;
import com.flypay.model.pojo.EquipmentInfoPO;
import org.springframework.stereotype.Repository;

@Repository
public class EquipmentInfoDaoImpl extends AbstractBaseDao<EquipmentInfoPO> implements EquipmentInfoDAO {
    @Override
    public EquipmentInfoPO save(EquipmentInfoPO eInfo) {
        return this.add(eInfo);
    }
}
