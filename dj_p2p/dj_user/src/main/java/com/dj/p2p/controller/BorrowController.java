package com.dj.p2p.controller;


import com.dj.p2p.base.ResultModel;
import com.dj.p2p.feign.WindControlClient;
import com.dj.p2p.pojo.AccountManager;
import com.dj.p2p.pojo.Bill;
import com.dj.p2p.pojo.BuyImmed;
import com.dj.p2p.pojo.project.MarkInfo;
import com.dj.p2p.pojo.project.ProjectVO;
import com.dj.p2p.pojo.token.UserToken;
import com.dj.p2p.service.RedisService;
import com.dj.p2p.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "借款相关Api")
public class BorrowController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;
    @Autowired
    private WindControlClient controlClient;


    @ApiOperation(value = "我要借钱的列表", notes = "我要借钱的列表")
    @GetMapping(value = "getborrowMoneys", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel getborrowMoneys(@RequestHeader("token") String token) throws Exception {

        UserToken userToken = redisService.get(token);
        List<ProjectVO> projectVO = userService.findBorrowerMoney(userToken.getId());

        return new ResultModel().success(projectVO);
    }


    @ApiOperation(value = "查看借款人的账单", notes = "查看借款人的账单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int")

    })
    @GetMapping(value = "checkBills", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel checkBills(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {

        UserToken userToken = redisService.get(token);
        List<Bill> billList = userService.findCheckBills(userToken.getId(), markId);

        return new ResultModel().success(billList);
    }

    @ApiOperation(value = "查看投资人的账单", notes = "查看投资人的账单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int")

    })
    @GetMapping(value = "lookInvestorBill", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel lookInvestorBill(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {

        UserToken userToken = redisService.get(token);
        List<Bill> billList = userService.findInvestorLookBill(userToken.getId(), markId);

        return new ResultModel().success(billList);
    }


    @ApiOperation(value = "我的出借列表", notes = "我的出借列表")
    @PostMapping(value = "myLoanList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel myLoanList(@RequestHeader("token") String token) throws Exception {

        UserToken userToken = redisService.get(token);
        List<ProjectVO> projectVOS = userService.myLoanList(userToken.getId());

        return new ResultModel().success(projectVOS);
    }


    @ApiOperation(value = "签约", notes = "签约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int")

    })
    @PostMapping(value = "signing", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel signing(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {

        UserToken userToken = redisService.get(token);
        userService.updatSigning(userToken.getId(), markId);

        return new ResultModel().success("success");
    }


    @ApiOperation(value = "我要出借的列表", notes = "我要出借的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int")
    })
    @PostMapping(value = "iWantLendIt", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel iWantLendIt(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {
        List<MarkInfo> data = controlClient.lendIt().getData();
        return new ResultModel().success(data);
    }


    @ApiOperation(value = "立即购买", notes = "立即购买")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "money", value = "出借金额", required = true, dataType = "double"),
            @ApiImplicitParam(name = "password", value = "交易密码", required = true, dataType = "String")
    })
    @PostMapping(value = "buyImmediately", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel buyImmediately(@RequestHeader("token") String token, @RequestParam("markId") Integer markId, @RequestParam("money") Double money, @RequestParam("password") String password) throws Exception {
        UserToken userToken = redisService.get(token);

        userService.updateBuy(userToken.getId(), markId,money,password);
        return new ResultModel().success("success");
    }


    @ApiOperation(value = "立即购买页面的数据", notes = "立即购买页面的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int")
    })
    @PostMapping(value = "getBuyImmedData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel getBuyImmedData(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {
        UserToken userToken = redisService.get(token);

        BuyImmed buyImmedData = userService.getBuyImmedData(markId, userToken.getId());
        return new ResultModel().success(buyImmedData);
    }

    @ApiOperation(value = "还款", notes = "还款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int")
    })
    @PostMapping(value = "repayment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel repayment(@RequestHeader("token") String token, @RequestParam("number") String number,@RequestParam("markId") Integer markId) throws Exception {
        UserToken userToken = redisService.get(token);
        userService.updateRepayment(number);
        return new ResultModel().success("success");
    }


    @ApiOperation(value = "根据用户id查询用户的资产信息", notes = "根据用户id查询用户的资产信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "用户的id", required = true, dataType = "int")
    })
    @PostMapping(value = "findAccByUserId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel<AccountManager> findAccByUserId(@RequestParam("userId") Integer markId) throws Exception {
        AccountManager accByUserId = userService.findAccByUserId(markId);
        return new ResultModel().success(accByUserId);
    }

    @ApiOperation(value = "根据用户id修改用户资产", notes = "根据用户id修改用户资产")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int")
    })
    @PostMapping(value = "updateAccById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel updateAccById(@RequestBody AccountManager accountManager) throws Exception {
        userService.updateAccById(accountManager);
        return new ResultModel().success("success");
    }




}
