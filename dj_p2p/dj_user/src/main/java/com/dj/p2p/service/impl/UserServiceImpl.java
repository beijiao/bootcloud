package com.dj.p2p.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.base.BusinessException;
import com.dj.p2p.cnum.BaseDataEnum;
import com.dj.p2p.common.AccountValidatorUtil;
import com.dj.p2p.common.MarkEnum;
import com.dj.p2p.common.PasswordSecurityUtil;
import com.dj.p2p.common.UUIDUtil;
import com.dj.p2p.common.constant.MSGConstant;
import com.dj.p2p.common.constant.NumberConstant;
import com.dj.p2p.common.constant.UserConstant;
import com.dj.p2p.feign.WindControlClient;
import com.dj.p2p.mapper.AccountManagerMapper;
import com.dj.p2p.mapper.AccountMapper;
import com.dj.p2p.mapper.UserMapper;
import com.dj.p2p.pojo.*;
import com.dj.p2p.pojo.project.Mark;
import com.dj.p2p.pojo.project.ProjectVO;
import com.dj.p2p.pojo.token.UserToken;
import com.dj.p2p.service.RedisService;
import com.dj.p2p.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountManagerMapper accountManagerMapper;

    @Autowired
    private RedisService redisService;

    private static final String VERIFY_BANK_CARD = "^\\d{19}$";

    /**
     * 用户注册
     *
     * @return
     * @throws Exception
     */
    @Override
    public Boolean registerUser(User user) throws Exception {
        String enCode32 = PasswordSecurityUtil.enCode32(user.getPassword());
        /**
         * 盐
         */
        String salt = PasswordSecurityUtil.generateSalt();
        String password = PasswordSecurityUtil.generatePassword(enCode32, salt);

        user.setPassword(password);
        user.setSalt(salt);

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", user.getPhone());
        User user1 = this.getOne(userQueryWrapper);
        if (!user.getIsWho().equals(NumberConstant.REDIS_EXPIRATION_TIME)) {
            if (user1 != null) {
                throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.USER_ALREADY_EXISTS);
            }
            user.setStatus(NumberConstant.ZERO);
            this.save(user);
        } else {
            user.setRole(UserConstant.ROLE_WIND_CONTROL_COMMISSIONER);
            this.save(user);
        }

        return true;
    }

    /**
     * 用户登录
     *
     * @param phone
     * @param password
     * @return
     */
    @Override
    public UserToken updateLoginUser(String phone, String password, Integer isWho) throws Exception {
        return updateGetUserToken(phone, password, isWho);
    }

    private UserToken updateGetUserToken(String phone, String password, Integer isWho) throws Exception {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", phone);
        String enCode32 = PasswordSecurityUtil.enCode32(password);
        User user = this.getOne(userQueryWrapper);

        return updateGetUserToken(isWho, enCode32, user);
    }

    private UserToken updateGetUserToken(Integer isWho, String enCode32, User user) throws Exception {
        if (!isWho.equals(NumberConstant.REDIS_EXPIRATION_TIME)) {
            if (user != null && true == PasswordSecurityUtil.checkPassword(enCode32, user.getPassword(), user.getSalt())) {
                if (user.getStatus().equals(UserConstant.LOCK)) {
                    throw new BusinessException(-2, UserConstant.CURRENTLY_LOCKED_ONE);
                }
                String token = UUIDUtil.getUUID();
                // 把user转成json字符串
                UserToken userToken = new UserToken();
                userToken.setId(user.getId());
                userToken.setToken(token);
                if (user.getNumber() != null && user.getName() != null) {
                    userToken.setRealName(String.valueOf(NumberConstant.SIX));
                }
                User user1 = new User();
                user1.setId(user.getId());
                user1.setLoginCount(user.getLoginCount() + NumberConstant.ONE);
                this.updateById(user1);

                userMapper.addLogin(new Date(), user.getId());
                redisService.set(token, userToken);

                /* redisService.pushHashAll(token, JSONObject.parseObject(jsonString, Map.class));*/
                redisService.expireKey(token, NumberConstant.REDIS_EXPIRATION_TIME);
                /*     addLoginScore(user.getId());*/
                return userToken;
            }
        }

        if (user != null && user.getRole() != null && true == PasswordSecurityUtil.checkPassword(enCode32, user.getPassword(), user.getSalt())) {
            if (user.getRole().equals(UserConstant.ROLE_DIRECTOR_OF_WIND_CONTROL) || user.getRole().equals(UserConstant.ROLE_WIND_CONTROL_COMMISSIONER) || user.getRole().equals(UserConstant.ROLE_WIND_CONTROL_MANAGER)) {
                String token = UUIDUtil.getUUID();
                UserToken userToken = new UserToken();
                userToken.setToken(token);
                userToken.setId(user.getId());
                userToken.setRealName(user.getName());
                if (user.getRole().equals(UserConstant.ROLE_DIRECTOR_OF_WIND_CONTROL)) {
                    userToken.setRole(UserConstant.ROLE_DIRECTOR_OF_WIND_CONTROL_STRING);
                }
                if (user.getRole().equals(UserConstant.ROLE_WIND_CONTROL_COMMISSIONER)) {
                    userToken.setRole(UserConstant.ROLE_WIND_CONTROL_COMMISSIONER_STRING);
                }
                if (user.getRole().equals(UserConstant.ROLE_WIND_CONTROL_MANAGER)) {
                    userToken.setRole(UserConstant.ROLE_WIND_CONTROL_MANAGER_STRING);
                }
                redisService.set(token, userToken, NumberConstant.REDIS_EXPIRATION_TIME);
                return userToken;
            } else {
                throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.ERROR_INCORRECT_USERNAME_OR_PASSWORD);
            }
        }
        throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.ERROR_INCORRECT_USERNAME_OR_PASSWORD);
    }


    /**
     * 是否实名认证
     *
     * @param id 用户id
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    @Override
    public User isAutonym(Integer id) throws Exception {
        User user = this.getById(id);
        if (!StringUtils.isEmpty(user.getName()) && !StringUtils.isEmpty(user.getNumber())) {
            return user;
        }
        return null;
    }

    /**
     * 实名认证
     *
     * @param name   真实姓名
     * @param number 身份证号
     * @param id     用户id
     * @return
     * @throws Exception
     */
    @Override
    public User userAutonym(String name, String number, Integer id) throws Exception, BusinessException {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("number", number);
        User user = this.getOne(userQueryWrapper);
        if (user != null) {
            return null;
        }
        if (!AccountValidatorUtil.isIDCard(number)) {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.INCORRECT_ID_NUMBER);
        }
        User user1 = new User();
        user1.setId(id);
        user1.setNumber(number);
        user1.setName(name);
        this.updateById(user1);
        return user1;

    }


    /**
     * 开户
     *
     * @param account
     * @param id      用户id
     * @return
     * @throws Exception
     */
    @Override
    public String openAccount(Account account, Integer id) throws Exception, BusinessException {

        boolean isNumber = AccountValidatorUtil.isIDCard(account.getNumber());
        if (isNumber != true) {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.ID_NUMBER_DOES_NOT_CONFORM_TO_THE_RULES);
        }
        if (true != account.getCardNo().matches(VERIFY_BANK_CARD)) {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.BANK_CARD_NUMBER_NINETEEN);
        }
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        Account account1 = accountMapper.selectOne(queryWrapper);
        if (account1 != null) {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.YOU_HAVE_OPENED_AN_ACCOUNT);

        }

        User user = this.getById(id);
        if (user != null) {
            if (user.getNumber().equals(account.getNumber())) {
                if (user.getRole() == null) {
                    user.setRole(account.getAccountType());
                    updateById(user);
                } else {
                    throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.YOU_HAVE_OPENED_AN_ACCOUNT);
                }

                account.setUserId(id);
                String uuid = UUIDUtil.getUUID();
                account.setVirtualAccount(uuid);
                accountMapper.insert(account);

                // 存金钱信息，一开始全是0
                AccountManager accountManager = new AccountManager();
                accountManager.setUserId(id);
                accountManager.setAccId(account.getId());
                accountManager.setTotalRevenue(NumberConstant.BIGDECIMEL_ZERO);
                accountManager.setAvailableBalance(NumberConstant.BIGDECIMEL_ZERO);
                accountManager.setTotalAsset(NumberConstant.BIGDECIMEL_ZERO);
                accountManager.setFreezeMoney(NumberConstant.BIGDECIMEL_ZERO);
                accountManager.setDueMoney(NumberConstant.BIGDECIMEL_ZERO);
                accountManager.setWaitRepay(NumberConstant.BIGDECIMEL_ZERO);
                accountManagerMapper.insert(accountManager);
                return uuid;
            } else {
                throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.INCORRECT_ID_NUMBER);
            }

        }

        return null;
    }

    /**
     * 安全中心页面需要的数据
     *
     * @param id 当前登录人的id
     * @return
     */
    @Override
    public SecurityCenterDataVO getSecurityCenterData(Integer id) throws Exception {


        SecurityCenterDataVO centerData = userMapper.getSecurityCenterData(id);
        // 判断是否实名认证
        if (!StringUtils.isEmpty(centerData.getRealName()) && !StringUtils.isEmpty(centerData.getIdCode())) {
            String idCode = centerData.getIdCode().replace(centerData.getIdCode().substring(NumberConstant.SIX, NumberConstant.FOURTEEN), "********");
            centerData.setIdCode(idCode);
            centerData.setIsReal(true);
        }
        // 判断是否开户
        if (!StringUtils.isEmpty(centerData.getOpenAccountCode())) {

            centerData.setOpenAccountStatus(true);

        }
        // 判断是否绑定手机号
        if (!StringUtils.isEmpty(centerData.getBindingPhone())) {
            centerData.setBindingPhone(centerData.getBindingPhone().substring(NumberConstant.ZERO, NumberConstant.THREE) + "****" + centerData.getBindingPhone().substring(7, centerData.getBindingPhone().length()));
            centerData.setIsBindingPhone(true);
        }
        // 判断是否绑定银行卡号
        if (!StringUtils.isEmpty(centerData.getBandingBankCard())) {
            String replace = centerData.getBandingBankCard().replace(centerData.getBandingBankCard().substring(NumberConstant.NINE, NumberConstant.FIFTEEN), "******");
            centerData.setBandingBankCard(replace);
            centerData.setIsBindingBankCard(true);

        }
        return centerData;
    }


    /**
     * 账户管理页面需要的数据
     *
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    @Override
    public AccountManager getAccountManagement(Integer userId) throws Exception {
        AccountManager accountManagement = userMapper.getAccountManagement(userId);
        // 通过用户获得用户角色
        /*// 如果为投资人
        if (accountManagement.getRole().equals(UserConstant.ROLE_INVESTOR)) {
            *//*
            *   投资人
                可用余额=总资产-冻结金额
                总收益：所有项目的利息收益相加
                总资产=冻结金额+可用余额
                待收金额=所有项目的投资收益相加
                待还金额=0
            *
            * *//*
            // 如果为借款人
*//*
            借款人
                    可用余额=借款金额没有提现的
            总收益=0
            总资产=待还金额+可用余额（如果可用余额是0的话，那么为负的待还金额）
            待收金额=0
            待还金额=所有借款项目相加*//*
        } else if (accountManagement.getRole().equals(UserConstant.ROLE_BORROWER)) {
            // 可用余额
            accountManagement.setAvailableBalance(accountManagement.getAvailableBalance());
            // 总收益
            accountManagement.setTotalRevenue(NumberConstant.BIGDECIMEL_ZERO);
            // 待收金额
            accountManagement.setDueMoney(NumberConstant.BIGDECIMEL_ZERO);
        }*/


        return accountManagement;
    }


    /**
     * 根据用户id充值页面需要的数据
     *
     * @param id
     * @return
     */
    @Override
    public BigDecimal upMoneyData(Integer id) throws Exception {
        AccountManager accountManagement = userMapper.getAccountManagement(id);
        return accountManagement.getAvailableBalance();
    }

    /**
     * 获取充值详情的数据
     *
     * @param id 用户id
     * @return
     * @throws Exception
     */
    @Override
    public UpMoneyDetails upMoneyDetails(Integer id) throws Exception {
        UpMoneyDetails upMoneyDetails = userMapper.getUpMoneyDetails(id);
        upMoneyDetails.setBankCode(upMoneyDetails.getBankCode().replace(upMoneyDetails.getBankCode().substring(NumberConstant.FOUR, NumberConstant.SIXTEEN), "************"));
        upMoneyDetails.setPhone(upMoneyDetails.getPhone().replace(upMoneyDetails.getPhone().substring(NumberConstant.THREE, NumberConstant.SEVEN), "****"));
        return upMoneyDetails;
    }

    /**
     * 确认充值
     *
     * @param id    用户id
     * @param money 充值金额
     */
    @Override
    public void updateConfirmSubmmit(Integer id, BigDecimal money, String password) throws Exception, BusinessException {
        AccountManager accountManager = userMapper.selectAccManager(id);
        if (password.equals(accountManager.getTransPassword())) {

            accountManager.setAvailableBalance(accountManager.getAvailableBalance().add(money));
            accountManagerMapper.updateById(accountManager);
        } else {
            throw new BusinessException(-NumberConstant.ERROR_CODE_TWO, MSGConstant.NOT_TRANSACTION_PASSWORD);
        }
    }

    /**
     * 提现页面要的数据
     *
     * @param id 用户id
     * @throws Exception
     */
    @Transactional(readOnly = true)
    @Override
    public DrawMoneyVO getDrawMoney(Integer id) throws Exception {
        DrawMoneyVO drawManyData = userMapper.getDrawManyData(id);
        drawManyData.setBankCode(drawManyData.getBankCode().replace(drawManyData.getBankCode().substring(NumberConstant.ZERO, NumberConstant.SIXTEEN), "***************"));
        drawManyData.setDrawMoneyType(MSGConstant.ORDINARY_WITHDRAWAL);
        return drawManyData;
    }


    /**
     * 提现
     *
     * @param id       用户id
     * @param password 交易密码
     * @param upMoney  提现金额
     * @throws Exception
     */
    @Override
    public void updateDrawMoney(Integer id, String password, BigDecimal upMoney) throws Exception {
        AccountManager accountManager = userMapper.selectAccManager(id);

        if (password.equals(accountManager.getTransPassword())) {
            if (accountManager.getAvailableBalance().doubleValue() > upMoney.doubleValue()) {
                accountManager.setAvailableBalance(accountManager.getAvailableBalance().subtract(upMoney));
                accountManagerMapper.updateById(accountManager);
            } else {
                throw new BusinessException(-NumberConstant.ERROR_CODE_TWO, MSGConstant.INSUFFICIENT_AVAILABLE_BALANCE);
            }
        } else {
            throw new BusinessException(-NumberConstant.ERROR_CODE_TWO, MSGConstant.NOT_TRANSACTION_PASSWORD);
        }

    }


    /**
     * 查询发标页面的借款人数据
     *
     * @throws Exception
     */
    @Override
    public List<UserVO> findBorrowerData() throws Exception {
        List<UserVO> borrowerData = userMapper.findBorrowerData(UserConstant.ROLE_BORROWER);
        //加编号
        for (int i = 0; i < borrowerData.size(); i++) {
            borrowerData.get(i).setNumber(i + 1);
        }
        return borrowerData;
    }

    /**
     * 查询风控那边用户信息的数据
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<UserInfo> findUserInfoData() throws Exception {
        return userMapper.findUserWindInfoData();
    }


    /**
     * 解锁
     *
     * @param userId
     * @throws Exception
     */
    @Override
    public void updateUnlocked(Integer userId) throws Exception {
        User user = this.getById(userId);

        if (user.getStatus().equals(UserConstant.LOCK)) {
            user.setStatus(UserConstant.UNLOCKED);
            this.updateById(user);

        } else {
            throw new BusinessException(-2, UserConstant.CURRENTLY_UNLOCKED);
        }
    }

    /**
     * 锁定
     *
     * @param userId
     * @throws Exception
     */
    @Override
    public void updateLocking(Integer userId) throws Exception {
        User user = this.getById(userId);

        if (user.getStatus().equals(UserConstant.UNLOCKED)) {
            user.setStatus(UserConstant.LOCK);
            this.updateById(user);

        } else {
            throw new BusinessException(-2, UserConstant.CURRENTLY_LOCKED);
        }
    }


    /**
     * 查借款人的账单
     *
     * @param id     用户id
     * @param markId 标id
     * @return
     * @throws Exception
     */
    @Override
    public List<Bill> findCheckBills(Integer id, Integer markId) throws Exception {
        List<Bill> billByUserId = accountMapper.findBillByUserId(id, markId, NumberConstant.ONE);
        for (Bill bill : billByUserId) {
            if (bill.getStatus().equals(UserConstant.NO_REPAYMENT)) {
                bill.setRepayMoney(UserConstant.NO_REPAYMENT_STRING);
            } else if (bill.getStatus().equals(UserConstant.REPAYMENT)) {
                bill.setRepayMoney(UserConstant.REPAYMENT_STRING);
            }
        }
        return billByUserId;
    }

    @Autowired
    private WindControlClient windControlClient;

    /**
     * 签约
     *
     * @param id     用户id
     * @param markId 标的id
     * @throws Exception
     */
    @Override
    public void updatSigning(Integer id, Integer markId) throws Exception {
        Mark mark = windControlClient.findMark(markId).getData();
        if (!mark.getIsSigning().equals(UserConstant.NOT_SIGNING)) {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.YOUR_IS_SIGNING);
        }
        userMapper.updateStatus(NumberConstant.ZERO, markId);
    }

    /**
     * 立即购买
     *
     * @param id     投资人id
     * @param markId 标id
     * @throws Exception
     */
    @Override
    public void updateBuy(Integer id, Integer markId, Double money, String password) throws Exception {
        QueryWrapper<AccountManager> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        // 根据用户id查询余额
        AccountManager accountManager = accountManagerMapper.selectOne(queryWrapper);
        Mark mark = windControlClient.findMark(markId).getData();
        User user = this.getById(id);
        if (!user.getRole().equals(BaseDataEnum.ROLE_INVESTOR.getId())) {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, "只有投资人才能投资");

        }

        if (mark.getCurrentMoney().equals(mark.getMoney())) {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, "筹款进度已满，不能再投资");
        }
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("user_id", id);
        Account account = accountMapper.selectOne(accountQueryWrapper);
        if (!password.equals(account.getTransPassword())) {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.NOT_TRANSACTION_PASSWORD);
        }
        BigDecimal moneys = new BigDecimal(String.valueOf(money));
        if (mark != null && accountManager != null) {
            if (moneys.doubleValue() < accountManager.getAvailableBalance().doubleValue()) {
                windControlClient.updateCurrentMoney(markId, money);
                // 剩余余额 = 当前余额减去借出余额
                BigDecimal decimal = accountManager.getAvailableBalance().subtract(moneys);
                accountManager.setAvailableBalance(decimal);
                // 冻结金额 = 冻结金额加借出金额
                BigDecimal freezeMoney = accountManager.getFreezeMoney().add(moneys);
                accountManager.setFreezeMoney(freezeMoney);
                // 每个月的利息收益
                BigDecimal moneysa = null;
                if (mark.getTerm().equals(MarkEnum.THREE.getId())) {
                    moneysa = mark.getYearInterestRate().divide(new BigDecimal(12.00), 2, RoundingMode.CEILING).multiply(new BigDecimal(3)).multiply(moneys).divide(NumberConstant.ONE_HUNDRED);
                }
                if (mark.getTerm().equals(MarkEnum.SIX.getId())) {
                    moneysa = mark.getYearInterestRate().divide(new BigDecimal(12.00), 2, RoundingMode.CEILING).multiply(new BigDecimal(6)).multiply(moneys).divide(NumberConstant.ONE_HUNDRED);

                }
                if (mark.getTerm().equals(MarkEnum.NINE.getId())) {
                    moneysa = mark.getYearInterestRate().divide(new BigDecimal(12.00), 2, RoundingMode.CEILING).multiply(new BigDecimal(9)).multiply(moneys).divide(NumberConstant.ONE_HUNDRED);

                }
                if (mark.getTerm().equals(MarkEnum.TWELVE.getId())) {
                    moneysa = mark.getYearInterestRate().divide(new BigDecimal(12.00), 2, RoundingMode.CEILING).multiply(new BigDecimal(12)).multiply(moneys).divide(NumberConstant.ONE_HUNDRED);

                }
                // 每个月的利率
                accountManager.setTotalRevenue(accountManager.getTotalRevenue().add(moneysa));
                accountManager.setTotalAsset(decimal.add(freezeMoney));

                // 待手金额
                BigDecimal zongShou = moneysa.add(moneys);
                accountManager.setDueMoney(accountManager.getDueMoney().add(zongShou));
                accountManager.setWaitRepay(NumberConstant.BIGDECIMEL_ZERO);
                accountManagerMapper.updateById(accountManager);
            } else {
                throw new BusinessException(NumberConstant.ERROR_CODE_TWO, "您的余额不足,请充值");
            }

        }

    }

    /**
     * 还钱
     *
     * @param number 账单表主键
     * @throws Exception
     */
    @Override
    public void updateRepayment(String number) throws Exception {
        Bill bill = userMapper.findBillByNumber(number);
        if (bill.getStatus().equals(NumberConstant.ONE)) {
            bill.setStatus(NumberConstant.ZERO);
        }

        QueryWrapper<AccountManager> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", bill.getUserId());
        AccountManager accountManager = accountManagerMapper.selectOne(queryWrapper);

        if (accountManager.getAvailableBalance().doubleValue() < bill.getPrincipalAndInterest().doubleValue()) {

            throw new BusinessException(-2, "余额不足请充值");
        }
        BigDecimal subtract = accountManager.getAvailableBalance().subtract(bill.getPrincipalAndInterest());
        accountManager.setAvailableBalance(subtract);
        accountManager.setTotalAsset(subtract);
        accountManagerMapper.updateById(accountManager);
        Mark mark = windControlClient.findMark(bill.getMarkId()).getData();
        QueryWrapper<AccountManager> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("user_id", mark.getBuyer());
        AccountManager accountManager2 = accountManagerMapper.selectOne(queryWrapper2);
        // 可用余额加
        accountManager2.setFreezeMoney(accountManager2.getFreezeMoney().subtract(bill.getPrincipalAndInterest()));
        // 冻结金额减一下
        accountManager2.setAvailableBalance(accountManager2.getAvailableBalance().add(bill.getPrincipalAndInterest()));

        accountManagerMapper.updateById(accountManager2);

    }


    /**
     * 查看投资人的账单信息
     *
     * @param id
     * @param markId
     * @return
     * @throws Exception
     */
    @Override
    public List<Bill> findInvestorLookBill(Integer id, Integer markId) throws Exception {
        List<Bill> billByUserId = accountMapper.findBillByUserId(id, markId, NumberConstant.ERROR_CODE_TWO_BIG);
        for (Bill bill : billByUserId) {
            if (bill.getStatus().equals(UserConstant.NO_REPAYMENT)) {
                bill.setRepayMoney(UserConstant.NO_REPAYMENT_STRING);
            } else if (bill.getStatus().equals(UserConstant.REPAYMENT)) {
                bill.setRepayMoney(UserConstant.REPAYMENT_STRING);
            }
        }
        return billByUserId;
    }

    /**
     * @param markId 用户id
     * @return
     * @throws Exception
     */
    @Override
    public AccountManager findAccByUserId(Integer markId) throws Exception {
        QueryWrapper<AccountManager> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", markId);


        return accountManagerMapper.selectOne(queryWrapper);
    }

    /**
     * 修改
     *
     * @param accountManager
     * @throws Exception
     */
    @Override
    public void updateAccById(AccountManager accountManager) throws Exception {

        accountManagerMapper.updateById(accountManager);
    }

    /**
     * 立即购买需要的数据
     *
     * @param markId
     */
    @Override
    public BuyImmed getBuyImmedData(Integer markId, Integer userId) throws Exception {

        BuyImmed markData = userMapper.findMarkData(markId);
        QueryWrapper<AccountManager> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        AccountManager accountManager = accountManagerMapper.selectOne(queryWrapper);
       /* if (markData.getIsQuota().equals(BidIssuingDataEnum.QUOTA.getId())) {
            BigDecimal bigDecimal = markData.getMoney().multiply(markData.getQuotaCount()).setScale(2, RoundingMode.CEILING).divide(NumberConstant.ONE_HUNDRED, 2, RoundingMode.CEILING);
            markData.setMaxQuota(bigDecimal);
        } else if (markData.getIsQuota().equals(BidIssuingDataEnum.OPEN_QUOTA.getId())) {*/
            markData.setMaxQuota(markData.getMoney());
       /* }*/
        BigDecimal speed = markData.getCurrentMoney().divide(markData.getMoney()).multiply(NumberConstant.BIGDECIMEL_ZERO);
        markData.setProgress(speed);
        markData.setBalance(accountManager.getAvailableBalance());

        return markData;
    }

    /**
     * 查询我要借款的列表
     *
     * @param id 借款人的用户id
     * @return
     */
    @Override
    public List<ProjectVO> findBorrowerMoney(Integer id) {
        List<ProjectVO> financialManagementProjects = userMapper.findFinancialManagementProjects(NumberConstant.ONE, id);
        return this.find(financialManagementProjects);
    }


    /**
     * 查询我的出借列表
     *
     * @param id 投资人的id
     * @return
     * @throws Exception
     */
    @Override
    public List<ProjectVO> myLoanList(Integer id) throws Exception {
        List<ProjectVO> financialManagementProjects = userMapper.findFinancialManagementProjects(NumberConstant.ERROR_CODE_TWO_BIG, id);

        return this.find(financialManagementProjects);
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
