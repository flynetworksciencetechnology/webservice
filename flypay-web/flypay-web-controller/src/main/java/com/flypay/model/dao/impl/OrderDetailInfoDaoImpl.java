package com.flypay.model.dao.impl;

import com.flypay.model.dao.OrderDetailInfoDao;
import com.flypay.model.dao.OrderInfoDao;
import com.flypay.model.dao.base.AbstractBaseDao;
import com.flypay.model.pojo.OrderDetailInfoPO;
import com.flypay.model.pojo.OrderInfoPO;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDetailInfoDaoImpl extends AbstractBaseDao<OrderDetailInfoPO> implements OrderDetailInfoDao {
    @Override
    public OrderDetailInfoPO save(OrderDetailInfoPO eInfo) {
        return this.add(eInfo);
    }
}
