package com.dj.p2p.wind.controller;

import com.dj.p2p.base.ResultModel;
import com.dj.p2p.pojo.User;
import com.dj.p2p.pojo.UserInfo;
import com.dj.p2p.wind.common.AccountValidatorUtil;
import com.dj.p2p.wind.common.constant.MSGConstant;
import com.dj.p2p.wind.common.constant.NumberConstant;
import com.dj.p2p.wind.feign.UserClient;
import com.dj.p2p.wind.service.RedisService;
import com.dj.p2p.wind.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "风控平台用户API")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RedisService redisService;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @PostMapping(value = "/windLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel login(@RequestParam("phone") String phone, @RequestParam("password") String password) throws Exception {
        Assert.hasText(password, MSGConstant.PASSWORD_CANNOT_BE_EMPTY);
        Assert.hasText(phone, MSGConstant.PHONE_CANNOT_BE_EMPTY);
        Assert.isTrue(AccountValidatorUtil.isMobile(phone), MSGConstant.ILLEGAL_MOBILE_PHONE_NUMBER);
        ResultModel login = userClient.login(phone, password, NumberConstant.REDIS_EXPIRATION_TIME);
        Assert.isTrue(login.getData() != null, MSGConstant.ERROR_INCORRECT_USERNAME_OR_PASSWORD);
        return new ResultModel().success(login.getData());
    }

    @ApiOperation(value = "风控平台注册", notes = "风控平台注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @PostMapping(value = "windRegister", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel windRegister(@RequestParam("phone") String phone, @RequestParam("password") String password) throws Exception {
        Assert.hasText(password, MSGConstant.PASSWORD_CANNOT_BE_EMPTY);
        Assert.hasText(phone, MSGConstant.PHONE_CANNOT_BE_EMPTY);
        Assert.isTrue(AccountValidatorUtil.isMobile(phone), MSGConstant.ILLEGAL_MOBILE_PHONE_NUMBER);
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setIsWho(NumberConstant.REDIS_EXPIRATION_TIME);
        ResultModel resultModel = userClient.addUser(user);
        if ("success".equals(resultModel.getMsg())) {

            return new ResultModel().success("success");
        }
        return new ResultModel().error("系统繁忙");

    }


    @ApiOperation(value = "用户信息", notes = "用户信息")
    @PostMapping(value = "findUserData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel findUserData(@RequestHeader("token") String token) throws Exception {
        List<UserInfo> data = userClient.getUserData().getData();
        return new ResultModel().success(data);
    }


    @ApiOperation(value = "锁定用户", notes = "锁定用户")
    @PostMapping(value = "lockingUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel lockingUser(@RequestHeader("token") String token, @RequestParam("userId")Integer userId) throws Exception {
        ResultModel locking = userClient.locking(userId);
        if ("success".equals(locking.getMsg())) {
            return new ResultModel().success("success");
        }
        return new ResultModel().error(locking.getMsg());
    }


    @ApiOperation(value = "解锁", notes = "解锁")
    @PostMapping(value = "unlockedUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel unlockedUser(@RequestHeader("token") String token, @RequestParam("userId")Integer userId) throws Exception {

        ResultModel unlocked = userClient.unlocked(userId);
        if ("success".equals(unlocked.getMsg())) {
            return new ResultModel().success("success");
        }
        return new ResultModel().error(unlocked.getMsg());
    }


}
