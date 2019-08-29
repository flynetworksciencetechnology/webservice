package com.flypay.model.dao.impl;

import com.flypay.model.dao.OrderInfoDao;
import com.flypay.model.dao.base.AbstractBaseDao;
import com.flypay.model.pojo.OrderInfoPO;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class OrderInfoDaoImpl extends AbstractBaseDao<OrderInfoPO> implements OrderInfoDao {
    @Override
    @Transactional
    public OrderInfoPO save(OrderInfoPO eInfo) {
        return this.add(eInfo);
    }

    @Override
    public OrderInfoPO findByOrderno(String orderno) {
        String SQL = "FROM OrderInfoPO WHERE orderno = ?";
        return this.load(SQL,orderno);
    }
}
