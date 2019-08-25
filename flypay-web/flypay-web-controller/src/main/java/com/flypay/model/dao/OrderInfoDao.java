package com.flypay.model.dao;

import com.flypay.model.dao.base.BaseDao;
import com.flypay.model.pojo.OrderInfoPO;

public interface OrderInfoDao extends BaseDao<OrderInfoPO> {
    OrderInfoPO save(OrderInfoPO eInfo);

    OrderInfoPO findByOrderno(String orderno);
}
