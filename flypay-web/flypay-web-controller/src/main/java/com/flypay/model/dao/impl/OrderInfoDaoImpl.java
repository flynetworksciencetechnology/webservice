package com.flypay.model.dao.impl;

import com.flypay.model.dao.EquipmentInfoDAO;
import com.flypay.model.dao.OrderInfoDao;
import com.flypay.model.dao.base.AbstractBaseDao;
import com.flypay.model.pojo.EquipmentInfoPO;
import com.flypay.model.pojo.OrderInfoPO;
import org.springframework.stereotype.Repository;

@Repository
public class OrderInfoDaoImpl extends AbstractBaseDao<OrderInfoPO> implements OrderInfoDao {
    @Override
    public OrderInfoPO save(OrderInfoPO eInfo) {
        return this.add(eInfo);
    }

    @Override
    public OrderInfoPO findByOrderno(String orderno) {
        String SQL = "SELECT * FROM order_info WHERE orderno = ?";
        return this.load(SQL,orderno);
    }
}
