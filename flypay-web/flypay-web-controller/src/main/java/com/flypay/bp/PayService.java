package com.flypay.bp;

import com.flypay.model.Result;

public interface PayService {

    Result getStoreMerchanInfo(String uuid, String ip);

    Result getWxpayfaceAuthinfo(String uuid,String amount,String orderno);

    Result initEquipmentInfo(String uuid);

    Result setRawdata(String uuid, String rawdata);

    Result pay(String uuid, String openid, String faceCode,String orderno);
}
