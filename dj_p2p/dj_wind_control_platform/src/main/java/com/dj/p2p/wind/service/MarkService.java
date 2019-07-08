package com.dj.p2p.wind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dj.p2p.pojo.ChargingMode;
import com.dj.p2p.pojo.project.Mark;
import com.dj.p2p.pojo.project.MarkInfo;
import com.dj.p2p.pojo.project.MarkVO;
import com.dj.p2p.pojo.project.ProjectVO;

import java.util.List;
import java.util.Map;

public interface MarkService extends IService<Mark> {


    /**
     * 发布标，进入初审状态
     *
     * @param project
     * @throws Exception
     */
    void addMark(Mark project) throws Exception;

    /**
     * 查询风控审核列表
     *
     * @return
     * @throws Exception
     */
    List<MarkVO> findWindControlList() throws Exception;

    /**
     * 初审页面需要的数据
     *
     * @param markId 标id
     * @return
     */
    Map<String, Object> findPreliminaryExamination(Integer markId, Integer isWho) throws Exception;

    /**
     * 初审同意
     *
     * @param markId
     */
    void updateFirstInstanceConsent(String realName, Integer markId) throws Exception;

    /**
     * 拒绝
     *
     * @param realName
     * @param markId
     * @throws Exception
     */
    void updateRefuse(String realName, Integer markId, Integer isWho) throws Exception;

    /**
     * 复审同意
     *
     * @param realName     审核人
     * @param chargingMode 审核信息实体
     * @param markId       标id
     * @throws Exception
     */
    void updateSecondInstanceConsent(String realName, ChargingMode chargingMode, Integer markId) throws Exception;

    /**
     * 查询理财项目列表
     *
     * @return
     * @throws Exception
     */
    List<ProjectVO> findFinancialManagementProjects() throws Exception;

    /**
     * 放款 该为还款中
     *
     * @param markId
     * @throws Exception
     */
    void updateLoan(Integer markId) throws Exception;

    /**
     * 我要借钱的列表
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<ProjectVO> findBorrowerMoney(Integer userId) throws Exception;

    /**
     * 根据id查询标信息
     *
     * @param markId
     * @return
     * @throws Exception
     */
    Mark findMarkById(Integer markId) throws Exception;

    /**
     * 我要出借的列表
     *
     * @return
     * @throws Exception
     */
    List<MarkInfo> findLendItList() throws Exception;

    void updateCurrentMoney(Integer markId, Double money) throws Exception;

    /**
     * 根据number查询账单信息
     *
     * @param number
     * @return
     * @throws Exception
     */
/*    Bill findBillById(String number) throws Exception;

    *//**
     * 修改
     *
     * @param number
     * @throws Exception
     *//*
    void updateBillById(String number) throws Exception;*/
}
