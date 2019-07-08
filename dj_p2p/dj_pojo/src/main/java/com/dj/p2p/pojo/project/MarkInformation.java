package com.dj.p2p.pojo.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class MarkInformation implements Serializable {


    /**
     * 标id
     */
    private Integer id;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GTM+8")
    private Date createTime;
    /**
     * 编号
     */
    private String number;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 借款人
     */
    private String borrower;

    /**
     * 起投金额
     */
    private BigDecimal startingThrow;

    /**
     * 借款金额
     */
    private BigDecimal loanAmount;

    /**
     * 年利率
     */
    private BigDecimal yearInterestRate;

    /**
     * 还款方式
     */
    private String paymentMethod;
    /**
     * 期限
     */
    private String term;
    /**
     * 状态
     */
    private String status;

    /**
     * 可投金额
     * <p>
     * may_cast
     */
    private BigDecimal mayCast;


}
