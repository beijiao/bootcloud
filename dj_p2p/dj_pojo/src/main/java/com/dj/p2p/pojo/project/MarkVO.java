package com.dj.p2p.pojo.project;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MarkVO implements Serializable {

    /**
     * 编号
     */
    private String number;
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


}

