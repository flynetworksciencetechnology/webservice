package com.flypay.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "order_detail_info")
public class OrderDetailInfoPO extends BasePO{
    @Column(name = "orderno")
    public String orderno;
    @Column(name = "od_name")
    public String odName;
    @Column(name = "amount")
    public Double amount;
    @Column(name = "status")
    public Integer status;
    public OrderDetailInfoPO() {
    }
    public OrderDetailInfoPO(String orderno, String odName, Double amount, Integer status) {
        this.orderno = orderno;
        this.odName = odName;
        this.amount = amount;
        this.status = status;
    }
}
