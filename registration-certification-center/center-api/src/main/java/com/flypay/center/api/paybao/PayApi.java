package com.flypay.center.api.paybao;

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
        value = "bao-pay"
)
public interface PayApi {
    @RequestMapping(value = "/payment/paybao", method = RequestMethod.POST)
    public String payment(@RequestParam("payKind") StaticConf.PAY_KIND payKind);
}

