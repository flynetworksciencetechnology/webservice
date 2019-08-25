package com.flypay.model.dao;

import com.flypay.model.dao.base.BaseDao;
import com.flypay.model.pojo.OrderDetailInfoPO;
import com.flypay.model.pojo.OrderInfoPO;

public interface OrderDetailInfoDao extends BaseDao<OrderDetailInfoPO> {
    OrderDetailInfoPO save(OrderDetailInfoPO eInfo);
}
