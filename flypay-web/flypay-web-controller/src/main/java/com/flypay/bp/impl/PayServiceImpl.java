package com.flypay.bp.impl;

import com.flypay.bp.PayService;
import com.flypay.center.api.conf.StaticConf;
import com.flypay.center.api.payservice.PayApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class PayServiceImpl implements PayService {

        @Autowired
        RestTemplate restTemplate;

        /**
         * 引入其它微服务暴露出来的api接口
         */
        @Autowired
        private PayApi payApi;

        @Override
        public String helloPay() {
            return "Hello Spring Cloud!";
        }

        @Override
        public String feginPay(String payType,String payKind) {
                switch (payType){
                        case "Wechat":
                                return payApi.paymentWechat(StaticConf.PAY_KIND.valueOf(payKind));
                        case "PayBao":
                                return payApi.paymentPaybao(StaticConf.PAY_KIND.valueOf(payKind));
                        default:
                                break;
                }
                return payApi.paymentWechat(StaticConf.PAY_KIND.valueOf(payKind));
        }

        @Override
        public String ribbonPay() {
            return restTemplate.getForObject("http://flypay-resource/demo/hello",String.class);
        }
}
