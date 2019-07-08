package com.dj.p2p.wind.feign;

import com.dj.p2p.base.ResultModel;
import com.dj.p2p.pojo.AccountManager;
import com.dj.p2p.pojo.User;
import com.dj.p2p.pojo.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping(value = "findBorrowerData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultModel findBorrowerData();

    @PostMapping(value = "login")
    ResultModel login(@RequestParam("phone") String phone, @RequestParam("password") String password, @RequestParam("isWho") Integer isWho);

    @GetMapping(value = "/getUserData")
    ResultModel<List<UserInfo>> getUserData();

    @PostMapping(value = "addUser")
    ResultModel addUser(@RequestBody User user);

    @PutMapping(value = "unlocked", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultModel unlocked(@RequestParam("userId") Integer userId);

    @ApiOperation(value = "锁定", notes = "锁定")
    @PutMapping(value = "locking", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultModel locking(@RequestParam("userId") Integer userId);


    @PostMapping(value = "findAccByUserId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultModel<AccountManager> findAccByUserId(@RequestParam("userId") Integer markId);

    @PostMapping(value = "updateAccById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultModel updateAccById(@RequestBody AccountManager accountManager);
}

