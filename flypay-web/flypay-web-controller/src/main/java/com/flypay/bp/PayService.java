package com.flypay.bp;

import com.flypay.model.Result;

public interface PayService {

    Result getBusinessInformation(String uuid);

    Result getWxpayfaceAuthinfo(String storeId, String storeName, String deviceId, String rawdata, String appid, String mchId,String key);
}
