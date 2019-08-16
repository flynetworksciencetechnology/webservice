package com.flypay.wechatpay.bp.impl;

import com.flypay.wechatpay.bp.WechatPayInterface;

public class WechatCodePayImpl implements WechatPayInterface {

    /**
     *
     * @return
     */
    @Override
    public String pay() {

        return "微信二维码付成功";
    }
}
