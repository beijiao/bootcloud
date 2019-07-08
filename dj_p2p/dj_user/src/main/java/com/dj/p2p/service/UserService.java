package com.dj.p2p.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dj.p2p.base.BusinessException;
import com.dj.p2p.pojo.*;
import com.dj.p2p.pojo.project.ProjectVO;
import com.dj.p2p.pojo.token.UserToken;

import java.math.BigDecimal;
import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @return
     * @throws Exception
     */
    Boolean registerUser(User user) throws Exception;

    /**
     * 用户登录
     *
     * @param phone
     * @param password
     * @return
     */
    UserToken updateLoginUser(String phone, String password, Integer isWho) throws Exception;

    /**
     * 是否实名认证
     *
     * @param id
     * @return
     * @throws Exception
     */
    User isAutonym(Integer id) throws Exception;


    /**
     * 实名认证
     *
     * @param name   真实姓名
     * @param number 身份证号
     * @param id
     * @return
     * @throws Exception
     */
    User userAutonym(String name, String number, Integer id) throws Exception, BusinessException;

    /**
     * 开户
     *
     * @param id 用户id
     * @return
     * @throws Exception
     */
    String openAccount(Account account, Integer id) throws Exception, BusinessException;

    /**
     * 安全中心页面需要的数据
     *
     * @param id
     * @return
     */
    SecurityCenterDataVO getSecurityCenterData(Integer id) throws Exception;

    /**
     * 账户管理页面需要的数据
     *
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    AccountManager getAccountManagement(Integer userId) throws Exception;

    /**
     * 根据用户id充值页面需要的数据
     *
     * @param id
     * @return
     */
    BigDecimal upMoneyData(Integer id) throws Exception;

    /**
     * 获取充值详情的数据
     *
     * @param id 用户id
     * @return
     * @throws Exception
     */
    UpMoneyDetails upMoneyDetails(Integer id) throws Exception;

    /**
     * 确认充值
     *
     * @param id    用户id
     * @param money 充值金额
     */
    void updateConfirmSubmmit(Integer id, BigDecimal money, String password) throws Exception, BusinessException;

    /**
     * 提现页面要的数据
     *
     * @param id 用户id
     * @throws Exception
     */
    DrawMoneyVO getDrawMoney(Integer id) throws Exception;

    /**
     * 提现
     *
     * @param id       用户id
     * @param password 交易密码
     * @param upMoney  提现金额
     * @throws Exception
     */
    void updateDrawMoney(Integer id, String password, BigDecimal upMoney) throws Exception;

    /**
     * 查询发标页面的借款人数据
     *
     * @throws Exception
     */
    List<UserVO> findBorrowerData() throws Exception;

    /**
     * 查询风控那边用户信息的数据
     *
     * @return
     * @throws Exception
     */
    List<UserInfo> findUserInfoData() throws Exception;

    /**
     * 解锁
     *
     * @param userId
     * @throws Exception
     */
    void updateUnlocked(Integer userId) throws Exception;

    /**
     * 锁定
     *
     * @param userId
     * @throws Exception
     */
    void updateLocking(Integer userId) throws Exception;

    /**
     * 查询我的借款列表
     *
     * @param id 借款人的id
     * @return
     * @throws Exception
     */
    List<ProjectVO> findBorrowerMoney(Integer id) throws Exception;

    /**
     * 查询我的出借列表
     *
     * @param id 投资人的id
     * @return
     * @throws Exception
     */
    List<ProjectVO> myLoanList(Integer id) throws Exception;

    /**
     * 查询投资人和借款人的账单
     *
     * @param id     用户id
     * @param markId 标id
     * @return
     * @throws Exception
     */
    List<Bill> findCheckBills(Integer id, Integer markId) throws Exception;

    /**
     * 签约
     *
     * @param id     用户id
     * @param markId 标的id
     * @throws Exception
     */
    void updatSigning(Integer id, Integer markId) throws Exception;

    /**
     * 立即购买
     *
     * @param id
     * @param markId
     * @throws Exception
     */
    void updateBuy(Integer id, Integer markId, Double money, String password) throws Exception;

    /**
     * 立即购买需要的数据
     *
     * @param markId
     */
    BuyImmed getBuyImmedData(Integer markId, Integer userId) throws Exception;

    /**
     * @param number 账单表主键
     * @throws Exception
     */
    void updateRepayment(String number) throws Exception;

    /**
     * 查看投资人的账单信息
     *
     * @param id
     * @param markId
     * @return
     * @throws Exception
     */
    List<Bill> findInvestorLookBill(Integer id, Integer markId) throws Exception;

    AccountManager findAccByUserId(Integer markId)throws Exception;

    /**
     * 修改
     * @param accountManager
     * @throws Exception
     */
    void updateAccById(AccountManager accountManager)throws Exception;
}
