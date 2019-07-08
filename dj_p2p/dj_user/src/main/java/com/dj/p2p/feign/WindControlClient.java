package com.dj.p2p.feign;

import com.dj.p2p.base.ResultModel;
import com.dj.p2p.pojo.project.Mark;
import com.dj.p2p.pojo.project.MarkInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient("wind-service")
public interface WindControlClient {


    @PostMapping(value = "findMark", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel<Mark> findMark(@RequestParam("markId") Integer markId);

    /**
     * 查询我要出借的列表
     *
     * @return
     */
    @PostMapping(value = "lendIt", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel<List<MarkInfo>> lendIt();


    @PutMapping(value = "updateCurrentMoney", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultModel updateCurrentMoney(@RequestParam("markId") Integer markId, @RequestParam("money") Double money);

}
