package com.dj.p2p.controller;

import com.dj.p2p.base.ResultModel;
import com.dj.p2p.common.constant.MSGConstant;
import com.dj.p2p.common.constant.NumberConstant;
import com.dj.p2p.common.constant.UserConstant;
import com.dj.p2p.feign.BaseDataClient;
import com.dj.p2p.pojo.*;
import com.dj.p2p.pojo.token.UserToken;
import com.dj.p2p.service.RedisService;
import com.dj.p2p.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 用户控制层类
 *
 * @author yinyw
 */
@RestController
@Api(tags = "用户Api")
public class UserController {

    @Autowired
    private UserService userservice;
    @Autowired
    private BaseDataClient baseDataClient;

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    private static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }


    @ApiOperation(value = "查询注册页面所有的基础数据", notes = "查询注册页面所有的基础数据 ")
    @GetMapping(value = "findBaseDataRegisterData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel findBaseDataRegisterData() {
        ResultModel baseData = baseDataClient.findBaseDataRegister();
        return new ResultModel().success(baseData.getData());
    }


    @ApiOperation(value = "开户页面所需的基础数据", notes = "开户页面所需的基础数据 ")
    @GetMapping(value = "openAccountData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel openAccountData() {
        ResultModel baseData = baseDataClient.openAccount();
        return new ResultModel().success(baseData.getData());
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @ApiImplicitParam(name = "user", value = "用户实体类", required = true, dataType = "User")
    @PostMapping(value = "addUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel addUser(@RequestBody User user) throws Exception {

        Assert.hasText(user.getPassword(), MSGConstant.PASSWORD_CANNOT_BE_EMPTY);
        Assert.hasText(user.getPhone(), MSGConstant.PHONE_CANNOT_BE_EMPTY);
        if (!user.getIsWho().equals(NumberConstant.REDIS_EXPIRATION_TIME)) {
            Assert.notNull(user.getHouse(), MSGConstant.PLEASE_CHOOSE_THE_HOUSE);
            Assert.notNull(user.getAge(), MSGConstant.PLEASE_ENTER_AGE);
            Assert.notNull(user.getSex(), MSGConstant.PLEASE_CHOOSE_GENDER);
            Assert.notNull(user.getEducation(), MSGConstant.PLEASE_CHOOSE_YOUR_EDUCATIONAL_BACKGROUND);
            Assert.notNull(user.getMarriage(), MSGConstant.PLEASE_CHOOSE_MARRIAGE);
            Assert.notNull(user.getWorkingCount(), MSGConstant.PLEASE_CHOOSE_YOUR_WORKING_LIFE);
            Assert.notNull(user.getYearIncome(), MSGConstant.PLEASE_CHOOSE_ANNUAL_INCOME);
            Assert.notNull(user.getValuation(), MSGConstant.PLEASE_CHOOSE_ASSET_VALUATION);
            Assert.notNull(user.getCar(), MSGConstant.PLEASE_CHOOSE_THE_CAR_PRODUCT);
        }
        if (!isMobile(user.getPhone())) {
            return new ResultModel().error(MSGConstant.ILLEGAL_MOBILE_PHONE_NUMBER);
        }
        Boolean isTrue = userservice.registerUser(user);
        if (isTrue) {
            return new ResultModel().success("success");
        }
        return new ResultModel().error(MSGConstant.MOBILE_NUMBER_ALREADY_EXISTS);
    }

    @Autowired
    private RedisService redisService;


    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel login(@RequestParam("phone") String phone, @RequestParam("password") String password, @RequestParam("isWho") Integer isWho) throws Exception {
        Assert.hasText(phone, MSGConstant.PHONE_CANNOT_BE_EMPTY);
        Assert.hasText(password, MSGConstant.PASSWORD_CANNOT_BE_EMPTY);
        UserToken user = userservice.updateLoginUser(phone, password, isWho);
        if (user != null) {
            return new ResultModel().success(user);
        }
        return new ResultModel().error(MSGConstant.ERROR_INCORRECT_USERNAME_OR_PASSWORD);
    }


    @GetMapping(value = "/user/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel noLogin() throws Exception {
        return new ResultModel().error(MSGConstant.NOT_LOGGED_IN);
    }


    @ApiOperation(value = "是否实名认证", notes = "是否实名认证")
    @PostMapping(value = "ifAutonym", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel ifAutonym(@RequestHeader("token") String token) throws Exception {

        UserToken userToken = redisService.get(token);
        /* Integer userId = redisService.getHash(token, "id");*/
        User user = userservice.isAutonym(userToken.getId());
        if (user != null) {
            return new ResultModel().success(MSGConstant.REAL_NAME_CERTIFICATION);
        }
        return new ResultModel().error(MSGConstant.UNREAL_NAME);
    }

    @ApiOperation(value = "解锁", notes = "解锁")
    @PutMapping(value = "unlocked", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel unlocked(@RequestParam("userId") Integer userId) throws Exception {
        userservice.updateUnlocked(userId);
        return new ResultModel().success("success");
    }

    @ApiOperation(value = "锁定", notes = "锁定")
    @PutMapping(value = "locking", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel locking(@RequestParam("userId") Integer userId) throws Exception {
        userservice.updateLocking(userId);
        return new ResultModel().success("success");
    }


    @ApiOperation(value = "实名认证", notes = "实名认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "真实姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "number", value = "身份证号", required = true, dataType = "String")
    })
    @PostMapping(value = "userAutonym", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel userAutonym(@RequestHeader("token") String token, @RequestParam("name") String name, @RequestParam("number") String number) throws Exception {

        Assert.hasText(name, MSGConstant.PLEASE_ENTER_YOUR_REAL_NAME);
        Assert.hasText(number, MSGConstant.PLEASE_ENTER_YOUR_ID_NUMBER);
        UserToken userToken = redisService.get(token);
        User user = userservice.userAutonym(name, number, userToken.getId());
        if (user != null) {
            return new ResultModel().success(MSGConstant.REAL_NAME_SUCCESS);
        }
        return new ResultModel().error(MSGConstant.ID_NUMBER_IS_IN_USE);
    }


    @ApiOperation(value = "开户", notes = "开户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "开户实体类信息", required = true, dataType = "Account"),
    })
    @PostMapping(value = "openAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel openAccount(@RequestBody Account account, @RequestHeader("token") String token) throws Exception {

        /*   Integer userId = redisService.getHash(token, "id");*/
        UserToken userToken = redisService.get(token);
        Integer userId = userToken.getId();

        String virtualAccount = userservice.openAccount(account, userId);
        if (virtualAccount != null) {
            return new ResultModel().success(virtualAccount);
        }
        return new ResultModel().error(MSGConstant.THE_SYSTEM_IS_BUSY_TRY_AGAN_LATER);
    }


    @ApiOperation(value = "安全中心页面需要的数据", notes = "安全中心页面需要的数据")
    @PostMapping(value = "getSecurityCenterData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel getSecurityCenterData(@RequestHeader("token") String token) throws Exception {
        /*        Integer userId = redisService.getHash(token, "id");*/
        UserToken userToken = redisService.get(token);
        Integer userId = userToken.getId();
        SecurityCenterDataVO centerData = userservice.getSecurityCenterData(userId);
        return new ResultModel().success(centerData);
    }


    @ApiOperation(value = "账户管理页面需要的数据", notes = "账户管理页面需要的数据")
    @GetMapping(value = "accountManagement", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel accountManagement(@RequestHeader("token") String token) throws Exception {
        /* Integer userId = redisService.getHash(token, "id");*/
        UserToken userToken = redisService.get(token);
        Integer userId = userToken.getId();
        AccountManager accountManagement = userservice.getAccountManagement(userId);
        return new ResultModel().success(accountManagement);
    }

    @ApiOperation(value = "充值页面需要的数据 ", notes = "充值页面需要的数据")
    @GetMapping(value = "upMoneyData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel upMoneyData(@RequestHeader("token") String token) throws Exception {

        UserToken userToken = redisService.get(token);
        Integer userId = userToken.getId();

        BigDecimal bigDecimal = userservice.upMoneyData(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("availableBalance", bigDecimal);
        return new ResultModel().success(map);
    }


    @ApiOperation(value = "充值 ", notes = "充值 ")
    @ApiImplicitParam(name = "money", value = "充值金额", required = true, dataType = "BigDecimal")
    @GetMapping(value = "upMoney", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel upMoney(@RequestHeader("token") String token, @RequestParam("money") BigDecimal money) throws Exception {
        Assert.notNull(money, MSGConstant.PLEASE_ENTER_THE_AMOUT_OF_RECHARGE);

        if (money.doubleValue() >= UserConstant.LOWEST_MONEY) {
            redisService.set(UserConstant.UP_MONEY, money);
            return new ResultModel().success("success");
        }
        return new ResultModel().error(MSGConstant.THE_MINIMUM_RECHARGE_AMOUNT_IS_TEN_YUAN);
    }


    @ApiOperation(value = "充值详情页面需要的数据 ", notes = "充值详情页面需要的数据")
    @GetMapping(value = "upMoneyDetails", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel upMoneyDetails(@RequestHeader("token") String token) throws Exception {
        /* Integer userId = redisService.getHash(token, "id");*/
        UserToken userToken = redisService.get(token);
        Integer userId = userToken.getId();
        BigDecimal money = redisService.get(UserConstant.UP_MONEY);
        UpMoneyDetails upMoneyDetails = userservice.upMoneyDetails(userId);
        upMoneyDetails.setUpMoney(money);
        return new ResultModel().success(upMoneyDetails);
    }


    @ApiOperation(value = "确认充值 ", notes = "确认充值")
    @ApiImplicitParam(name = "password", value = "交易密码", required = true, dataType = "String")
    @GetMapping(value = "confirmSubmmit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel confirmSubmmit(@RequestHeader("token") String token, @RequestParam("password") String password) throws Exception {
        Assert.hasText(password, MSGConstant.PLEASE_TRANSACTION_PASSWORD);
        //充值金额
        /*Integer userId = redisService.getHash(token, "id");*/
        UserToken userToken = redisService.get(token);
        Integer userId = userToken.getId();
        BigDecimal money = redisService.get(UserConstant.UP_MONEY);
        userservice.updateConfirmSubmmit(userId, money, password);
        return new ResultModel().success(MSGConstant.SUCCESSFUL_RECHARGE);


    }


    @ApiOperation(value = "提现页面需要的数据 ", notes = "提现页面需要的数据")
    @GetMapping(value = "drawMoneyData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel drawMoneyData(@RequestHeader("token") String token) throws Exception {
        /*   Integer userId = redisService.getHash(token, "id");*/
        UserToken userToken = redisService.get(token);
        Integer userId = userToken.getId();
        DrawMoneyVO drawMoney = userservice.getDrawMoney(userId);
        return new ResultModel().success(drawMoney);
    }

    @ApiOperation(value = "提现", notes = "提现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "交易密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "upMoney", value = "提现金额", required = true, dataType = "String")
    })
    @PutMapping(value = "drawMoney", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel drawMoney(@RequestHeader("token") String token, @RequestParam("password") String password, @RequestParam("upMoney") BigDecimal upMoney) throws Exception {
        Assert.hasText(password, MSGConstant.PLEASE_TRANSACTION_PASSWORD);
        Assert.notNull(upMoney, MSGConstant.PLEASE_ENTER_THE_AMOUNT_OF_CASH_WITHDRAWAL);
        /* Integer userId = redisService.getHash(token, "id");*/
        UserToken userToken = redisService.get(token);
        Integer userId = userToken.getId();
        userservice.updateDrawMoney(userId, password, upMoney);
        return new ResultModel().success("success");
    }


    @ApiOperation(value = "查询发标页面的借款人数据", notes = "查询发标页面的借款人数据")
    @GetMapping(value = "findBorrowerData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel findBorrowerData() throws Exception {
        List<UserVO> borrowerData = userservice.findBorrowerData();
        return new ResultModel().success(borrowerData);
    }


    @ApiOperation(value = "实名提示", notes = "实名提示")
    @GetMapping(value = "/user/realName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel realName() throws Exception {

        return new ResultModel().error("请您实名认证！");
    }

    @ApiOperation(value = "风控那边的用户信息", notes = "风控那边的用户信息")
    @GetMapping(value = "/getUserData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel<List<UserInfo>> getUserData() throws Exception {
        List<UserInfo> userInfoData = userservice.findUserInfoData();
        return new ResultModel().success(userInfoData);
    }




}
