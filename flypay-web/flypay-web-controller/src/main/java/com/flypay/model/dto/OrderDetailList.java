package com.flypay.model.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailList {
    public List<OrderDetailDTO> goods_detail;

    public OrderDetailList() {
        goods_detail = new ArrayList<>();
    }
}
