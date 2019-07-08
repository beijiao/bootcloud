package com.dj.p2p.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dj.p2p.pojo.*;
import com.dj.p2p.pojo.project.ProjectVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.Date;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {


    /**
     * 查询登录人的安全中心所需的数据
     *
     * @param id
     * @return
     */
    SecurityCenterDataVO getSecurityCenterData(Integer id) throws Exception;

    /**
     * 账户管理页面需要的数据
     *
     * @param userId 用户id
     */
    AccountManager getAccountManagement(Integer userId) throws Exception;

    /**
     * 充值详情的数据
     *
     * @param id
     * @return
     */
    UpMoneyDetails getUpMoneyDetails(Integer id) throws Exception;

    /**
     * 查询该用户的账户的钱数
     *
     * @param id
     * @return
     */
    AccountManager selectAccManager(Integer id) throws Exception;

    /**
     * 查询提现页面数据
     *
     * @param userId 用户id
     * @return
     */
    DrawMoneyVO getDrawManyData(Integer userId) throws Exception;


    /**
     * 查询发标页面的借款人数据
     *
     * @param roleBorrower
     * @return
     */
    List<UserVO> findBorrowerData(Integer roleBorrower) throws Exception;


    /**
     * 查询风控那边用户信息的数据
     *
     * @return
     * @throws DataAccessException
     */
    List<UserInfo> findUserWindInfoData() throws Exception;

    /**
     * 增加登录记录
     *
     * @param date
     * @param id
     */
    void addLogin(@Param("date") Date date, @Param("userId") Integer id);


    /**
     * 查询理财项目列表
     *
     * @return
     * @throws DataAccessException
     */
    List<ProjectVO> findFinancialManagementProjects(@Param("isWho") Integer isWho, @Param("userId") Integer userId) throws DataAccessException;


    /**
     * 用户签约
     *
     * @param markId
     */
    void updateStatus(@Param("status")Integer status, @Param("markId") Integer markId);

    /**
     * 查询标数据
     * @param markId
     * @return
     */
    BuyImmed  findMarkData(Integer markId);

    /**
     *
     * @param number
     * @return
     * @throws DataAccessException
     */
    Bill findBillByNumber(String number) throws DataAccessException;
}
