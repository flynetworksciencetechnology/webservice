package com.flypay.model.dto;

public class OrderDetailDTO {

    public String goods_id;
    public String goods_name;
    public Integer goods_num;
    public Integer price;

    public OrderDetailDTO(String goods_id, String goods_name, Integer goods_num, Integer price) {
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.goods_num = goods_num;
        this.price = price;
    }
}
