package com.dj.p2p.wind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.base.BusinessException;
import com.dj.p2p.pojo.AccountManager;
import com.dj.p2p.pojo.Bill;
import com.dj.p2p.pojo.ChargingMode;
import com.dj.p2p.pojo.project.*;
import com.dj.p2p.wind.common.MarkEnum;
import com.dj.p2p.wind.common.PasswordSecurityUtil;
import com.dj.p2p.wind.common.constant.MSGConstant;
import com.dj.p2p.wind.common.constant.MarkConstant;
import com.dj.p2p.wind.common.constant.NumberConstant;
import com.dj.p2p.wind.feign.UserClient;
import com.dj.p2p.wind.mapper.ApprovalRecordsMapper;
import com.dj.p2p.wind.mapper.ChargingModeMapper;
import com.dj.p2p.wind.mapper.MarkMapper;
import com.dj.p2p.wind.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MarkServiceImpl extends ServiceImpl<MarkMapper, Mark> implements MarkService {


    @Autowired
    private ChargingModeMapper chargingModeMapper;

    @Autowired
    private UserClient userClient;


    @Autowired
    private ApprovalRecordsMapper approvalRecordsMapper;

    /**
     * 发布标，进入初审状态
     *
     * @param project
     * @throws Exception
     */
    @Override
    public void addMark(Mark project) throws Exception {
        project.setCreateTime(new Date());
        project.setStatus(MarkConstant.IN_THE_FIRST_INSTANCE);
        /*if(project.getIsQuota().equals(BidIssuingDataEnum.QUOTA.getId())){



        }else {
            project.setQuotaCount(null);
        }*/
        this.save(project);
    }


    /**
     * 查询风控审核列表
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<MarkVO> findWindControlList() throws Exception {

        // 查询风控审核列表
        List<MarkVO> windControlList = this.baseMapper.findWindControlList();
        for (int i = 0; i < windControlList.size(); i++) {
            MarkVO markVO = windControlList.get(i);
            if (!markVO.getStatus().equals(MarkConstant.REFUSE_STRING) && !markVO.getStatus().equals(MarkConstant.IN_THE_FIRST_INSTANCE_STRING) && !markVO.getStatus().equals(MarkConstant.REVIEW_IN_PROGRESS_STRING)) {
                markVO.setStatus(MarkConstant.SUCCESS);
            }
            markVO.setNumber("DJ" + (i + NumberConstant.ONE));
        }

        return windControlList;
    }


    /**
     * 初审页面需要的数据
     *
     * @param markId 标id
     * @return
     */
    @Override
    public Map<String, Object> findPreliminaryExamination(Integer markId, Integer isWho) throws Exception {
        // 查询 项目详情
        ProjectInfo projectInfo = this.baseMapper.findProjectInfo(markId);
        //查询审核记录
        List<ApprovalRecords> approvalRecords = this.baseMapper.findApprovalRecords(markId);
        // 标信息
        MarkInformation markInfo = this.baseMapper.findMarkInfo(markId);

        Map<String, Object> map = new HashMap<>(NumberConstant.SIXTEEN);
        if (isWho != null) {
            List<LoanContract> loanContract = this.baseMapper.findLoanContract();
            List<LoanContract> chargeType = this.baseMapper.findChargeType(MarkConstant.CHARGE_TYPE);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            map.put("salesTime", sdf.format(date));
            map.put("chargeType", chargeType);
            map.put("loanContract", loanContract);
        }
        map.put("projectInfo", projectInfo);
        map.put("approvalRecords", approvalRecords);
        map.put("markInfo", markInfo);
        return map;
    }

    /**
     * 初审同意 添加轨迹 修改状态为复审中
     *
     * @param realName
     * @param markId
     * @throws Exception
     */
    @Override
    public void updateFirstInstanceConsent(String realName, Integer markId) throws Exception {

        Mark mark = new Mark();
        mark.setId(markId);
        mark.setStatus(MarkConstant.REVIEW_IN_PROGRESS);
        mark.setPersonCount(NumberConstant.ZERO);
        this.updateById(mark);
        ApprovalRecords records = new ApprovalRecords();
        records.setName(realName);
        records.setStatus(String.valueOf(MarkConstant.REVIEW_IN_PROGRESS));
        records.setOpinion(MarkConstant.AGREE);
        records.setCreateTime(new Date());
        /*records.setApprovalTime(new Date());*/
        records.setMarkId(markId);
        this.baseMapper.addRecords(records);
    }


    /**
     * 初审拒绝
     *
     * @param realName
     * @param markId
     * @throws Exception
     */
    @Override
    public void updateRefuse(String realName, Integer markId, Integer isWho) throws Exception {
        Mark mark = new Mark();
        mark.setId(markId);
        if (isWho != null) {

            mark.setStatus(MarkConstant.IN_THE_FIRST_INSTANCE_REFUSE);

        } else {
            mark.setStatus(MarkConstant.REVIEW_IN_PROGRESS_REFUSE);

        }

        this.updateById(mark);
        QueryWrapper<ApprovalRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mark_id", markId);
        queryWrapper.eq("status", MarkConstant.REVIEW_IN_PROGRESS);
        ApprovalRecords approvalRecords = approvalRecordsMapper.selectOne(queryWrapper);

        if (approvalRecords != null) {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.THT_BID_HAS_BEEN_PROCESSEDHE);
        }

        ApprovalRecords records = new ApprovalRecords();
        records.setName(realName);

        if (isWho != null) {
            records.setStatus(String.valueOf(MarkConstant.IN_THE_FIRST_INSTANCE));
        } else {
            records.setStatus(String.valueOf(MarkConstant.REVIEW_IN_PROGRESS));
        }

        records.setOpinion(MarkConstant.REFUSE_STRING);
        records.setCreateTime(new Date());
        records.setMarkId(markId);
        this.baseMapper.addRecords(records);
    }


    /**
     * 复审同意
     *
     * @param realName     审核人
     * @param chargingMode 审核信息实体
     * @param markId       标id
     * @throws Exception
     */
    @Override
    public void updateSecondInstanceConsent(String realName, ChargingMode chargingMode, Integer markId) throws Exception {

        Mark mark = new Mark();
        mark.setId(markId);

        mark.setStatus(MarkConstant.COMPLETE);
        mark.setCurrentMoney(NumberConstant.BIGDECIMEL_ZERO);
        this.updateById(mark);
        /*chargingMode.setStatus(MarkEnum.BORROWING.getId());*/
        chargingMode.setSpeedProgress(NumberConstant.ZERO);
        chargingMode.setSalesTime(new Date());
        chargingModeMapper.insert(chargingMode);

    }


    /**
     * 查询理财项目列表
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<ProjectVO> findFinancialManagementProjects() throws Exception {
        // 查询理财项目列表
        List<ProjectVO> projects = this.baseMapper.findFinancialManagementProjects(NumberConstant.CODE_TWO, null);
        return this.find(projects);
    }


    /**
     * 放款 该为还款中
     *
     * @param markId
     * @throws Exception
     */
    @Override
    public void updateLoan(Integer markId) throws Exception {
        Mark mark1 = this.getById(markId);
        // 判断当前状态是不是待放款
        if (mark1 != null && mark1.getStatus().equals(MarkEnum.TO_BE_LENT.getId())) {
            if (mark1.getIsSigning().equals(MarkConstant.SIGNING)) {
                Mark mark = new Mark();
                mark.setId(markId);
                // 把标的状态改为还款中
                mark.setStatus(MarkEnum.AMONG_REPAYMENTS.getId());
                // 放款时间
                mark.setLoanTime(new Date());
                this.updateById(mark);
                AccountManager accountManager = userClient.findAccByUserId(mark1.getBorrowerId()).getData();
                BigDecimal add = accountManager.getWaitRepay().add(accountManager.getAvailableBalance());
                accountManager.setTotalAsset(add);
                accountManager.setWaitRepay(accountManager.getWaitRepay().add(mark1.getMoney()));
                accountManager.setDueMoney(NumberConstant.BIGDECIMEL_ZERO);
                accountManager.setTotalRevenue(NumberConstant.BIGDECIMEL_ZERO);
                userClient.updateAccById(accountManager);

                this.baseMapper.addBorrowMoney(mark1.getBorrowerId(), mark1.getMoney());

                addBill(markId, mark1);

            } else {
                throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.NOT_SIGNING_STRING);
            }

        } else {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.NOT_EXITS);
        }
    }

    private void addBill(Integer markId, Mark mark1) {
        // 如果是三期
        if (mark1.getTerm().equals(MarkEnum.THREE.getId())) {


            // 借款人的集合
            List<Bill> billList = new ArrayList<>();
            // 投资人人的集合
            List<Bill> billList1 = new ArrayList<>();

            // 每个月的本金
            BigDecimal money = mark1.getMoney().divide(new BigDecimal(3), 2, RoundingMode.CEILING);
            // 每个月的利率
            BigDecimal interestRate = mark1.getYearInterestRate().divide(new BigDecimal(12.00), 2, RoundingMode.CEILING);
            BigDecimal interest = money.multiply(interestRate).divide(NumberConstant.ONE_HUNDRED).setScale(2, RoundingMode.CEILING);
            // 每个月所还的本息
            BigDecimal total = money.add(interest).setScale(2, RoundingMode.CEILING);


            for (int i = 0; i < 3; i++) {
                String count = (i + 1) + "/" + 3;
                String number = "DJ" + (i + 1) + PasswordSecurityUtil.generateRandom(9);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(i == 0 ? new Date() : billList.get(i - 1).getMaturityTime());
                calendar.add(Calendar.MONTH, 1);//当前时间前去一个月，即一个月前的时间
                Bill bill1 = new Bill();
                bill1.setNumber(number);
                bill1.setPrincipalAndInterest(total);
                bill1.setPrincipal(money);
                bill1.setInterest(interest);
                bill1.setMaturityTime(calendar.getTime());
                bill1.setUserId(mark1.getBorrowerId());
                bill1.setStatus(1);
                bill1.setTermCount(count);
                bill1.setMarkId(markId);
                billList.add(bill1);
            }
            this.baseMapper.addBillList(billList);

            for (int i = 0; i < 3; i++) {

                String count = (i + 1) + "/" + 3;
                String number = "DJ" + (i + 1) + PasswordSecurityUtil.generateRandom(9);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(i == 0 ? new Date() : billList.get(i - 1).getMaturityTime());
                calendar.add(Calendar.MONTH, 1);//当前时间前去一个月，即一个月前的时间
                Bill bill1 = new Bill();
                bill1.setNumber(number);
                bill1.setPrincipalAndInterest(total);
                bill1.setPrincipal(money);
                bill1.setInterest(interest);
                bill1.setStatus(1);
                bill1.setMaturityTime(calendar.getTime());
                bill1.setTermCount(count);
                bill1.setMarkId(markId);

                bill1.setUserId(mark1.getBuyer());
                billList1.add(bill1);
            }


//            this.baseMapper.addBillList(billList1);

        }
        // 如果是6期
        if (mark1.getTerm().equals(MarkEnum.SIX.getId())) {


            // 借款人的集合
            List<Bill> billList = new ArrayList<>();
            // 投资人人的集合
            List<Bill> billList1 = new ArrayList<>();

            // 每个月的本金
            BigDecimal money = mark1.getMoney().divide(new BigDecimal(6), 2, RoundingMode.CEILING);
            // 每个月的利率
            BigDecimal interestRate = mark1.getYearInterestRate().divide(new BigDecimal(12.00), 2, RoundingMode.CEILING);
            BigDecimal interest = money.multiply(interestRate).divide(NumberConstant.ONE_HUNDRED).setScale(2, RoundingMode.CEILING);
            // 每个月所还的本息
            BigDecimal total = money.add(interest).setScale(2, RoundingMode.CEILING);

            for (int i = 0; i < 6; i++) {
                Date date = new Date();
                String count = (i + 1) + "/" + 6;
                String number = "DJ" + (i + 1) + PasswordSecurityUtil.generateRandom(9);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(i == 0 ? new Date() : billList.get(i - 1).getMaturityTime());
                calendar.add(Calendar.MONTH, 1);//当前时间前去一个月，即一个月前的时间
                Bill bill1 = new Bill();
                bill1.setNumber(number);
                bill1.setPrincipalAndInterest(total);
                bill1.setPrincipal(money);
                bill1.setInterest(interest);
                bill1.setMaturityTime(calendar.getTime());
                bill1.setUserId(mark1.getBorrowerId());
                bill1.setStatus(1);
                bill1.setTermCount(count);
                bill1.setMarkId(markId);

                billList.add(bill1);
            }

            this.baseMapper.addBillList(billList);
            for (int i = 0; i < 6; i++) {

                String count = (i + 1) + "/" + 6;
                String number = "DJ" + (i + 1) + PasswordSecurityUtil.generateRandom(9);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(i == 0 ? new Date() : billList.get(i - 1).getMaturityTime());
                calendar.add(Calendar.MONTH, 1);//当前时间前去一个月，即一个月前的时间
                Bill bill1 = new Bill();
                bill1.setNumber(number);
                bill1.setPrincipalAndInterest(total);
                bill1.setPrincipal(money);
                bill1.setInterest(interest);
                bill1.setStatus(1);
                bill1.setMaturityTime(calendar.getTime());
                bill1.setTermCount(count);
                bill1.setMarkId(markId);

                bill1.setUserId(mark1.getBuyer());
                billList1.add(bill1);
            }


            /*this.baseMapper.addBillList(billList1);*/

        }

        // 如果  是9期
        if (mark1.getTerm().equals(MarkEnum.NINE.getId())) {

            // 借款人的集合
            List<Bill> billList = new ArrayList<>();
            // 投资人人的集合
            List<Bill> billList1 = new ArrayList<>();

            // 每个月的本金
            BigDecimal money = mark1.getMoney().divide(new BigDecimal(9), 2, RoundingMode.CEILING);
            // 每个月的利率
            BigDecimal interestRate = mark1.getYearInterestRate().divide(new BigDecimal(12.00), 2, RoundingMode.CEILING);
            BigDecimal interest = money.multiply(interestRate).divide(NumberConstant.ONE_HUNDRED).setScale(2, RoundingMode.CEILING);
            // 每个月所还的本息
            BigDecimal total = money.add(interest).setScale(2, RoundingMode.CEILING);

            for (int i = 0; i < 9; i++) {

                String count = (i + 1) + "/" + 9;
                String number = "DJ" + (i + 1) + PasswordSecurityUtil.generateRandom(9);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(i == 0 ? new Date() : billList.get(i - 1).getMaturityTime());
                calendar.add(Calendar.MONTH, 1);//当前时间前去一个月，即一个月前的时间
                Bill bill1 = new Bill();
                bill1.setNumber(number);
                bill1.setPrincipalAndInterest(total);
                bill1.setPrincipal(money);
                bill1.setInterest(interest);
                bill1.setMaturityTime(calendar.getTime());
                bill1.setUserId(mark1.getBorrowerId());
                bill1.setStatus(1);
                bill1.setTermCount(count);
                bill1.setMarkId(markId);

                billList.add(bill1);
            }
            this.baseMapper.addBillList(billList);

            for (int i = 0; i < 9; i++) {
                String count = (i + 1) + "/" + 9;
                String number = "DJ" + (i + 1) + PasswordSecurityUtil.generateRandom(9);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(i == 0 ? new Date() : billList.get(i - 1).getMaturityTime());
                calendar.add(Calendar.MONTH, 1);//当前时间前去一个月，即一个月前的时间
                Bill bill1 = new Bill();
                bill1.setNumber(number);
                bill1.setPrincipalAndInterest(total);
                bill1.setPrincipal(money);
                bill1.setInterest(interest);
                bill1.setStatus(1);
                bill1.setMaturityTime(calendar.getTime());
                bill1.setTermCount(count);
                bill1.setMarkId(markId);

                bill1.setUserId(mark1.getBuyer());
                billList1.add(bill1);
            }

//            this.baseMapper.addBillList(billList1);
        }

        // 如果是12期
        if (mark1.getTerm().equals(MarkEnum.TWELVE.getId())) {
            // 借款人的集合
            List<Bill> billList = new ArrayList<>();
            // 投资人人的集合
            List<Bill> billList1 = new ArrayList<>();

            // 每个月的本金
            BigDecimal money = mark1.getMoney().divide(new BigDecimal(12), 2, RoundingMode.CEILING);
            // 每个月的利率
            BigDecimal interestRate = mark1.getYearInterestRate().divide(new BigDecimal(12.00), 2, RoundingMode.CEILING);
            BigDecimal interest = money.multiply(interestRate).divide(NumberConstant.ONE_HUNDRED).setScale(2, RoundingMode.CEILING);
            // 每个月所还的本息
            BigDecimal total = money.add(interest).setScale(2, RoundingMode.CEILING);

            for (int i = 0; i < 12; i++) {

                String count = (i + 1) + "/" + 12;
                String number = "DJ" + (i + 1) + PasswordSecurityUtil.generateRandom(9);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(i == 0 ? new Date() : billList.get(i - 1).getMaturityTime());
                calendar.add(Calendar.MONTH, 1);//当前时间前去一个月，即一个月前的时间
                Bill bill1 = new Bill();
                bill1.setNumber(number);
                bill1.setPrincipalAndInterest(total);
                bill1.setPrincipal(money);
                bill1.setInterest(interest);
                bill1.setMaturityTime(calendar.getTime());
                bill1.setUserId(mark1.getBorrowerId());
                bill1.setStatus(1);
                bill1.setTermCount(count);
                bill1.setMarkId(markId);

                billList.add(bill1);
            }
            this.baseMapper.addBillList(billList);

            for (int i = 0; i < 12; i++) {
                String count = (i + 1) + "/" + 12;
                String number = "DJ" + (i + 1) + PasswordSecurityUtil.generateRandom(9);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(i == 0 ? new Date() : billList.get(i - 1).getMaturityTime());
                calendar.add(Calendar.MONTH, 1);//当前时间前去一个月，即一个月前的时间
                Bill bill1 = new Bill();
                bill1.setNumber(number);
                bill1.setPrincipalAndInterest(total);
                bill1.setPrincipal(money);
                bill1.setInterest(interest);
                bill1.setStatus(1);
                bill1.setMaturityTime(calendar.getTime());
                bill1.setTermCount(count);
                bill1.setMarkId(markId);

                bill1.setUserId(mark1.getBuyer());
                billList1.add(bill1);
            }


            /*    this.baseMapper.addBillList(billList1);*/

        }
    }

    /**
     * 根据id查询标信息
     *
     * @param markId
     * @return
     * @throws Exception
     */
    @Override
    public Mark findMarkById(Integer markId) throws Exception {

        return this.getById(markId);
    }

    /**
     * 我要出借的列表
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<MarkInfo> findLendItList() throws Exception {
        List<MarkInfo> lendItList = this.baseMapper.findLendItList();
        return lendItList;
    }


    @Override
    public void updateCurrentMoney(Integer markId, Double money) throws Exception {
        Mark mark = this.getById(markId);
        if (mark.getMoney().equals(money)) {
            mark.setStatus(MarkEnum.TO_BE_LENT.getId());
        }
        mark.setPersonCount(mark.getPersonCount() + NumberConstant.ONE);
        mark.setCurrentMoney(mark.getCurrentMoney().add(new BigDecimal(String.valueOf(money))));
        this.updateById(mark);
    }

    /**
     * 我要借钱的列表
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<ProjectVO> findBorrowerMoney(Integer userId) throws Exception {
        List<ProjectVO> projects = this.baseMapper.findFinancialManagementProjects(NumberConstant.ONE, userId);
        return this.find(projects);
    }

    private List<ProjectVO> find(List<ProjectVO> projects) {
        for (int i = 0; i < projects.size(); i++) {
            ProjectVO projectVO = projects.get(i);
            projectVO.setNumber("DJ" + (i + NumberConstant.ONE));
            // 算筹款进度
            BigDecimal multiply = projectVO.getCurrentMoney().divide(projectVO.getLoanAmount()).multiply(new BigDecimal(100.0));
            projectVO.setSpeedProgress(multiply.intValue());

            if (multiply.equals(NumberConstant.ONE_HUNDRED)) {
                projectVO.setStatus(MarkEnum.TO_BE_LENT.getName());

            }
        }
        return projects;
    }
}
