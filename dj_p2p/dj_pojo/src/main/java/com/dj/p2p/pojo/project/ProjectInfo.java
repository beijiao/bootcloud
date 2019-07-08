package com.dj.p2p.pojo.project;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 项目详情
 */
@Data
public class ProjectInfo implements Serializable {


    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 年龄
     */
    private Integer age;
    /**
     * 学历
     */
    private String edu;
    /**
     * 性别
     */
    private String sex;
    /**
     * 婚姻
     */
    private String marriage;
    /**
     * 工作年限
     */
    private String workingCount;
    /**
     * valuation
     * 资产估值
     */
    private BigDecimal valuation;
    /**
     * 房产情况
     */
    private String house;
    /**
     * 车产
     */
    private String car;
    /**
     * 年收入
     */
    private String yearIncome;
    /**
     * 借款详情
     */
    private String loanDesc;


}
