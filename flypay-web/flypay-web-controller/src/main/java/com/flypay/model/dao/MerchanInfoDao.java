package com.flypay.model.dao;

import com.flypay.model.dao.base.BaseDao;
import com.flypay.model.pojo.MerchanInfoPO;

import java.util.List;

public interface MerchanInfoDao extends BaseDao<MerchanInfoPO> {

    List<MerchanInfoPO> findAll();

    MerchanInfoPO findById(Long id);
}
