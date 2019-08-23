package com.flypay.bp;

import com.flypay.model.Result;

public interface PayService {

    Result getBusinessInformation(String uuid);

    Result getWxpayfaceAuthinfo(String uuid);

    Result initEquipmentInfo(String uuid);

    Result setRawdata(String uuid, String rawdata);
}
