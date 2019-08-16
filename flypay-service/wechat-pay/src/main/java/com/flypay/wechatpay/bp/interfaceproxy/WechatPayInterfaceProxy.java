package com.flypay.wechatpay.bp.interfaceproxy;


import com.flypay.center.api.conf.StaticConf;
import com.flypay.wechatpay.bp.WechatPayInterface;
import com.flypay.wechatpay.bp.impl.WechatCodePayImpl;
import com.flypay.wechatpay.bp.impl.WechatFacePayImpl;

public class WechatPayInterfaceProxy {

    public WechatPayInterface wp;

    public WechatPayInterfaceProxy(StaticConf.PAY_KIND payKind) {
        switch (payKind){
            case CODE:
                wp = new WechatCodePayImpl();
                break;
            case FACE:
                wp = new WechatFacePayImpl();
                break;
            default:
                wp = new WechatFacePayImpl();
                break;
        }
    }
    public String pay() {

        return wp.pay();
    }
}
