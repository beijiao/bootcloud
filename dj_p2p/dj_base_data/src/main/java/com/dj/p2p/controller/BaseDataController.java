package com.dj.p2p.base.controller;

import com.dj.p2p.base.ResultModel;
import com.dj.p2p.pojo.BaseDataVo;
import com.dj.p2p.service.BaseDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 基础数据控制器
 */

@RestController
@Api(tags = "基础数据Api")
public class BaseDataController {


    @Autowired
    private BaseDataService baseDataService;

    @ApiOperation(value = "查询注册页面所有的基础数据", notes = "查询注册页面所有的基础数据 ")
    @GetMapping(value = "findBaseDataRegister", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel findBaseDataRegister() throws Exception {
        Map<String, List<BaseDataVo>> map = baseDataService.findBaseDataRegister();
        return new ResultModel().success(map);
    }

    @ApiOperation(value = "开户页面所需的基础数据", notes = "开户页面所需的基础数据 ")
    @GetMapping(value = "openAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel openAccount() throws Exception {
        List<BaseDataVo> list = baseDataService.findopenAccount();
        return new ResultModel().success(list);
    }

    @ApiOperation(value = "发标页面需要的基础数据", notes = "发标页面需要的基础数据")
    @GetMapping(value = "getBidIssuingData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel getBidIssuingData() throws Exception {
        Map<String, List<BaseDataVo>> bidIssuingData = baseDataService.findBidIssuingData();
        return new ResultModel().success(bidIssuingData);
    }


}
