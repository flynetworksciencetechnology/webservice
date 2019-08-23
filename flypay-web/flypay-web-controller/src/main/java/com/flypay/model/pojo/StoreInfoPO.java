package com.flypay.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "store_info")
public class StoreInfoPO extends BasePO {

    /**
     * 门店名字
     */
    @Column(name ="store_name")
    public String storeName;
    /**
     * 品牌
     */
    @Column(name ="brand")
    public String brand;
    /**
     * 所属城市
     */
    @Column(name ="city")
    public String city;
    /**
     * 所属商户id
     */
    @Column(name = "merchant_id")
    public Long merchantId;
    /**
     * 绑定设备id
     */
    @Column(name = "equip_id")
    public Long equipId;
    /**
     * 是否根门店,如果是则不可编辑默认生成,并且不可见,默认为true
     */
    @Column(name = "is_root")
    public Boolean isRoot = true;
}
