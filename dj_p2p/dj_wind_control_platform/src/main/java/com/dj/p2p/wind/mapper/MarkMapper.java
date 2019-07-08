package com.dj.p2p.wind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dj.p2p.pojo.Bill;
import com.dj.p2p.pojo.project.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.List;

public interface MarkMapper extends BaseMapper<Mark> {


    /**
     * 查询风控审核列表
     */
    List<MarkVO> findWindControlList() throws DataAccessException;

    /**
     * 查询标名称信息
     */
    MarkInformation findMarkInfo(Integer markId) throws DataAccessException;

    /**
     * 查询项目详情
     */
    ProjectInfo findProjectInfo(Integer markId) throws DataAccessException;

    /**
     * 查询审核记录
     */
    List<ApprovalRecords> findApprovalRecords(Integer markId) throws DataAccessException;

    /**
     * 增加审核轨迹
     *
     * @param records
     * @throws DataAccessException
     */
    void addRecords(@Param("records") ApprovalRecords records) throws DataAccessException;


    /**
     * 查询借款合同
     */
    List<LoanContract> findLoanContract() throws DataAccessException;


    /**
     * 查询借款手续费收费方式
     */
    List<LoanContract> findChargeType(Integer pId) throws DataAccessException;


    /**
     * 查询理财项目列表
     *
     * @return
     * @throws DataAccessException
     */
    List<ProjectVO> findFinancialManagementProjects(@Param("isWho") Integer isWho, @Param("userId") Integer userId) throws DataAccessException;

    /**
     * 给借款人打钱
     *
     * @param borrowerId 借款人id
     * @param money      金额
     * @throws DataAccessException
     */
    void addBorrowMoney(@Param("userId") Integer borrowerId, @Param("money") BigDecimal money) throws DataAccessException;

    /**
     * 添加账单
     *
     * @param billList
     */
    void addBillList(List<Bill> billList) throws DataAccessException;

    /**
     * @return
     */
    List<MarkInfo> findLendItList() throws DataAccessException;


}
