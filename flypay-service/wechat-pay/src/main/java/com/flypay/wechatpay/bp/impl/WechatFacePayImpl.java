package com.flypay.wechatpay.bp.impl;

import com.flypay.conf.StaticConf;
import com.flypay.wechatpay.PayApi;
import com.flypay.wechatpay.bp.WechatPayInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatFacePayImpl implements WechatPayInterface {

    /**
     *
     * @return
     */
    @Override
    public String pay() {

        return "微信刷脸支付成功";
    }
}
