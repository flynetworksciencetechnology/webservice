package com.flypay.center.api.wechat;

import com.flypay.center.api.conf.StaticConf;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @FeignClient中value的名称要跟flypay-resource项目中application.properties文件的name保持一致
 * @author Administrator
 *
 */
@FeignClient(
        value = "wechat-pay"
)
public interface PayApi {
    @RequestMapping(value = "/payment/wechat", method = RequestMethod.POST)
    public String payment(@RequestParam("payKind") StaticConf.PAY_KIND payKind);
}
