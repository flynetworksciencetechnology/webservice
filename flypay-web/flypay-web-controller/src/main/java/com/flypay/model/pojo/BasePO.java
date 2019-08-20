package com.flypay.model.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * 超类
 */
@MappedSuperclass
public class BasePO implements Serializable {

    /**
     * 主键id,主键规则生成
     */
    @Id
    public Long id;
    @Column(name = "add_time",nullable = false)
    public Date addTime;
    @Column(name = "update_time",nullable = false)
    public Date updateTime;
}
