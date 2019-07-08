package com.dj.p2p.feign;

import com.dj.p2p.base.ResultModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
/**
 *
 * @author yyw
 */
@FeignClient("base-service")
public interface BaseDataClient {


    /**
     * 查询注册页面所有的基础数据
     * @return
     */
    @GetMapping(value = "findBaseDataRegister", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel findBaseDataRegister();


    /**
     * 开户页面所需的基础数据
     * @return
     */
    @GetMapping(value = "openAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel openAccount();

}
