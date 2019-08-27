package com.flypay.bp;

import com.flypay.model.Result;

public interface PayService {

    Result getStoreMerchanInfo(String uuid, String ip);

    Result getWxpayfaceAuthinfo(String uuid,String rawdata,String amount);

    Result initEquipmentInfo(String uuid, String ip);

    Result setRawdata(String uuid, String rawdata, String ip);

    Result pay(String uuid, String openid, String faceCode,String orderno, String ip);

    Result getAuthinfo(String uuid, String amount, String ip);
}
