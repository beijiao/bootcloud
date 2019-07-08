package com.dj.p2p.pojo.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 借款标发布表
 */
@Data
@TableName("p2p_mark")
public class Mark implements Serializable {


    /**
     * 加入人次
     */
    private Integer personCount;

    /**
     * 是否签约
     */
    private Integer isSigning;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 产品名称
     */
    private Integer product;

    /**
     * 标的状态
     */
    private Integer status;

    /**
     * 是否显示标
     */

    private Integer isDisplayMark;

    /**
     * 标的类型
     */
    private Integer markType;


    /**
     * 借款人的id
     */
    private Integer borrowerId;

    /**
     * 借款金额
     */
    private BigDecimal money;
    /**
     * 是否限额
     */
    private Integer isQuota;
    /**
     * 年利率 %
     */
    private BigDecimal yearInterestRate;
    /**
     * 期限
     */
    private Integer term;
    /**
     * 还款方式
     */
    private Integer paymentMethod;
    /**
     * 项目名称
     */
    private String projectName;


    /**
     * 借款说明
     */
    private String loanDesc;

    /**
     * 发布时间
     */
    private Date createTime;

    /**
     * 筹款进度
     */
    private BigDecimal currentMoney;

    /**
     * 放款时间
     */
    private Date loanTime;


    /**
     *
     */
    private Integer buyer;

    /**
     * 限额多少
     */
    private BigDecimal quotaCount;
}
