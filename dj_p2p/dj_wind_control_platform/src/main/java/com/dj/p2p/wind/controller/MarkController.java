package com.dj.p2p.wind.controller;

import com.dj.p2p.base.ResultModel;
import com.dj.p2p.pojo.ChargingMode;
import com.dj.p2p.pojo.project.Mark;
import com.dj.p2p.pojo.project.MarkInfo;
import com.dj.p2p.pojo.project.MarkVO;
import com.dj.p2p.pojo.project.ProjectVO;
import com.dj.p2p.pojo.token.UserToken;
import com.dj.p2p.wind.common.AccountValidatorUtil;
import com.dj.p2p.wind.common.constant.MSGConstant;
import com.dj.p2p.wind.common.constant.MarkConstant;
import com.dj.p2p.wind.common.constant.NumberConstant;
import com.dj.p2p.wind.feign.BaseDataClient;
import com.dj.p2p.wind.feign.UserClient;
import com.dj.p2p.wind.service.MarkService;
import com.dj.p2p.wind.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yyw
 * 借款相关的接口
 */
@RestController
@Api(tags = "借款相关的接口")
public class MarkController {

    @Autowired
    private MarkService projectService;
    @Autowired
    private BaseDataClient baseDataClient;

    @Autowired
    private UserClient userClient;


    @Autowired
    private RedisService redisService;

    @ApiOperation(value = "发标", notes = "发标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mark", value = "发布标的详细信息", required = true, dataType = "Mark")
    })
    @PostMapping(value = "addMark", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel addMark(@RequestBody Mark mark, @RequestHeader("token") String token) throws Exception {
        Assert.notNull(mark.getProduct(), MSGConstant.PLEASE_CHOOSE_THE_PRODUCT);
        Assert.notNull(mark.getIsDisplayMark(), MSGConstant.PLEASE_CHOOSE_WHETHER_TO_DISPLAY_THE_LABEL_OR_NOT);
        Assert.notNull(mark.getMarkType(), MSGConstant.PLEASE_SELECT_THE_TYPE_OF_SUBJECT_MATTER);
        Assert.notNull(mark.getBorrowerId(), MSGConstant.RELEVANT_BORROWERS);
        Assert.notNull(mark.getMoney(), MSGConstant.BORROW_MONTY_MSG_IS_NOT_NULL);
        Assert.notNull(mark.getIsQuota(), MSGConstant.IS_THERE_A_LIMIT);
        Assert.notNull(mark.getYearInterestRate(), MSGConstant.PLEASE_CHOOSE_THE_TIME_IMIT);
        Assert.notNull(mark.getTerm(), MSGConstant.PLEASE_CHOOSE_THE_TIME_IMIT);
        Assert.notNull(mark.getPaymentMethod(), MSGConstant.PLEASE_CHOOSE_THE_METHOD_OF_REPAYMENT);
        Assert.hasText(mark.getProjectName(), MSGConstant.PLEASE_CHOOSE_PROJECT_NAME);
        Assert.hasText(mark.getLoanDesc(), MSGConstant.PLEASE_CHOOSE_LENDING_NOTES);
        boolean positiveInteger = AccountValidatorUtil.isPositiveInteger(String.valueOf(mark.getMoney().doubleValue()));
        if (positiveInteger != true || mark.getMoney().doubleValue() < NumberConstant.TWENTY_THOUSANDHE || mark.getMoney().doubleValue() > NumberConstant.MILLIONTO) {
            return new ResultModel().error(MSGConstant.THT_AMOUNT_RANGES_FORM_TWENTY_THOUSANDHE_TO_MILLIONTO);
        }
        projectService.addMark(mark);
        return new ResultModel().success("success");
    }

    @ApiOperation(value = "查询风控审核列表", notes = "查询风控审核列表")
    @GetMapping(value = "findWindControlList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel findWindControlList(@RequestHeader("token") String token) throws Exception {
        List<MarkVO> windControlList = projectService.findWindControlList();
        return new ResultModel().success(windControlList);
    }

    @ApiOperation(value = "发标页面需要的基础数据", notes = "发标页面需要的基础数据")
    @GetMapping(value = "findBidIssuingData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel getBidIssuingData() throws Exception {
        ResultModel bidIssuingData = baseDataClient.getBidIssuingData();
        ResultModel borrowerData = userClient.findBorrowerData();
        Map<String, Object> map = new HashMap<>();
        map.put("borrower_data", borrowerData.getData());
        map.put("bidIssuing_page_data", bidIssuingData.getData());
        return new ResultModel().success(map);
    }


    @ApiOperation(value = "增加标的筹款金额", notes = "增加标的筹款金额")
    @PutMapping(value = "updateCurrentMoney", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel updateCurrentMoney(@RequestParam("markId") Integer markId,@RequestParam("money") Double money) throws Exception {

       projectService.updateCurrentMoney(markId,money);
        return new ResultModel().success("success");
    }


    @ApiOperation(value = "初审页面需要的数据", notes = "初审页面需要的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "初审核标的id", required = true, dataType = "int"),
    })
    @GetMapping(value = "findPreliminaryExamination", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel findPreliminaryExamination(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {
        Map<String, Object> preliminaryExamination = projectService.findPreliminaryExamination(markId, null);
        return new ResultModel().success(preliminaryExamination);
    }


    @ApiOperation(value = "初审同意", notes = "初审同意")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int"),
    })
    @PostMapping(value = "firstInstanceConsent", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel firstInstanceConsent(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {
        UserToken user = redisService.get(token);

        projectService.updateFirstInstanceConsent(user.getRealName(), markId);
        return new ResultModel().success("success");
    }


    @ApiOperation(value = "初审拒绝", notes = " 初审拒绝")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int"),
    })
    @PostMapping(value = "refuse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel refuse(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {
        UserToken user = redisService.get(token);
        projectService.updateRefuse(user.getRealName(), markId, MarkConstant.IN_THE_FIRST_INSTANCE);
        return new ResultModel().success("success");
    }

    @ApiOperation(value = "复审拒绝", notes = " 复审拒绝")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int"),
    })
    @PostMapping(value = "secondRefuse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel secondRefuse(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {
        UserToken user = redisService.get(token);
        projectService.updateRefuse(user.getRealName(), markId, null);
        return new ResultModel().success("success");
    }


    @ApiOperation(value = "复审页面需要的数据", notes = "复审页面需要的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int"),
    })
    @GetMapping(value = "findSecondPreliminaryExamination", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel findSecondPreliminaryExamination(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {
        Map<String, Object> preliminaryExamination = projectService.findPreliminaryExamination(markId, NumberConstant.REDIS_EXPIRATION_TIME);
        return new ResultModel().success(preliminaryExamination);
    }


    @ApiOperation(value = "根据id查标信息", notes = "根据id查标信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int"),
    })
    @PostMapping(value = "findMark", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel<Mark> findMark(@RequestParam("markId") Integer markId) throws Exception {

        Mark markById = projectService.findMarkById(markId);
        return new ResultModel().success(markById);
    }


    @ApiOperation(value = "复审同意", notes = "复审同意")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "chargingMode", value = "同意信息的实体详细信息类", required = true, dataType = "ChargingMode")
    })
    @PostMapping(value = "secondInstanceConsent", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel secondInstanceConsent(@RequestHeader("token") String token, @RequestBody ChargingMode chargingMode, @RequestParam("markId") Integer markId) throws Exception {
        UserToken user = redisService.get(token);
        projectService.updateSecondInstanceConsent(user.getRealName(), chargingMode, markId);
        return new ResultModel().success("success");
    }


    @ApiOperation(value = "放款", notes = "放款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int")

    })
    @PostMapping(value = "loan", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel loan(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {
        projectService.updateLoan(markId);

        return new ResultModel().success("success");
    }

    @ApiOperation(value = "理财项目列表", notes = "理财项目列表")
    @PostMapping(value = "findFinancialManagementProjects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel findFinancialManagementProjects(@RequestHeader("token") String token) throws Exception {
        List<ProjectVO> financialManagementProjects = projectService.findFinancialManagementProjects();
        return new ResultModel().success(financialManagementProjects);
    }


   /* @ApiOperation(value = "我要借钱的列表", notes = "我要借钱的列表")
    @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int")
    @PostMapping(value = "borrowMoney", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel borrowMoney(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {
        *//*  projectService.updateLoan(markId);*//*
        UserToken userToken = redisService.get(token);
        List<ProjectVO> projectVOS = projectService.findBorrowerMoney(1);

        return new ResultModel().success(projectVOS);
    }*/

  @ApiOperation(value = "我要出借的列表", notes = "我要出借的列表")
 @PostMapping(value = "lendIt", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
   public ResultModel<List<MarkInfo>> lendIt() throws Exception {
     List<MarkInfo> lendItList = projectService.findLendItList();
      return new ResultModel().success(lendItList);
   }







/*

    @ApiOperation(value = "我要借钱的列表", notes = "我要借钱的列表")
    @ApiImplicitParam(name = "markId", value = "审核标的id", required = true, dataType = "int")
    @PostMapping(value = "borrowMoney", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultModel borrowMoney(@RequestHeader("token") String token, @RequestParam("markId") Integer markId) throws Exception {
        *//*  projectService.updateLoan(markId);*//*
        UserToken userToken = redisService.get(token);
        List<ProjectVO> projectVOS = projectService.findBorrowerMoney(1);

        return new ResultModel().success(projectVOS);
    }*/


}
