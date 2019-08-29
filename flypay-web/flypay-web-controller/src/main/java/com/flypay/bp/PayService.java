package com.flypay.bp;

import com.flypay.model.Result;

public interface PayService {

    Result getWxpayfaceAuthinfo(String uuid,String rawdata,String orderno);
    Result initEquipmentInfo(String uuid, String ip);
    Result pay(String uuid, String openid, String faceCode,String orderno);
    Result creatorder(String uuid, String amount);
}
