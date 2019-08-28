package com.flypay.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "order_info")
public class OrderInfoPO extends BasePO{
    @Column(name = "orderno")
    public String orderno;
    @Column(name = "total_amount")
    public Double totalAmount;
    @Column(name = "pay_time")
    public Date payTime;
    @Column(name = "status")
    public Integer status;
    @Column(name = "store_id")
    public String storeId;
    @Column(name = "transaction_id")
    public String transactionId;
    @Column(name = "fee")
    public String fee;

    public OrderInfoPO(String orderno, Double totalAmount, Date payTime, Integer status, String storeId) {
        this.orderno = orderno;
        this.totalAmount = totalAmount;
        this.payTime = payTime;
        this.status = status;
        this.storeId = storeId;
    }
    public OrderInfoPO() {
    }
}
