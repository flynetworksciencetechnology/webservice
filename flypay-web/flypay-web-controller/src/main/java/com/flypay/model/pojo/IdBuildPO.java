package com.flypay.model.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * id生成类
 */
@Entity
@Table(name = "id_build")
public class IdBuildPO extends BasePO{
    /**
     * 主键id,主键规则生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @Column(name = "table_name",nullable = false,unique = true)
    public String tableName;
    @Column(name = "current_no",nullable = false)
    public Long currentNo;
    @Column(name = "add_time",nullable = false)
    public Date addTime;
    @Column(name = "update_time",nullable = false)
    public Date updateTime;
}
