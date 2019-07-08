package com.dj.p2p.wind.feign;

import com.dj.p2p.base.ResultModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("base-service")
public interface BaseDataClient {

    @GetMapping(value = "getBidIssuingData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultModel getBidIssuingData();


}
