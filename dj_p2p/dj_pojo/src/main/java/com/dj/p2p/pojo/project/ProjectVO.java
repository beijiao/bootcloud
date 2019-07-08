package com.dj.p2p.pojo.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yinyw
 */
@Data
public class ProjectVO implements Serializable {


    /**
     * 编号
     */
    private String number;

    private Integer id;

    /**
     * 项目
     */
    private String projectName;
    /**
     * 借款人
     */
    private String borrower;

    /**
     * 本息合计
     */
    private BigDecimal totalPrincipalAndInterest;

    /**
     * 借款金额
     */
    private BigDecimal loanAmount;

    /**
     * 利息金额
     */
    private BigDecimal interestAmount;


    /**
     * 年利率
     */
    private BigDecimal yearInterestRate;

    /**
     * 期限
     */
    private String term;
    /**
     * 状态
     */
    private String status;

    /**
     * 发售时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GTM+8")
    private Date salesTime;

    /**
     * 筹款进度
     */
    private Integer speedProgress;

    /**
     * 当前筹款金额
     */
    private BigDecimal currentMoney;


    /**
     * fundraising_time
     */
    private Integer fundraisingTime;

    /**
     * 放款时间
     */
    private Date loanTime;



}
