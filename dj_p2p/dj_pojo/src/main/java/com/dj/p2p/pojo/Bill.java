package com.dj.p2p.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bill")
public class Bill implements Serializable {

    @TableId(type = IdType.INPUT)
    private String number;

    /**
     * 标 的id
     */
    private Integer markId;

    /**
     * 几期
     */
    private String termCount;

    /**
     * 应还本息
     */
    private BigDecimal principalAndInterest;

    /**
     * 本金
     */
    private BigDecimal principal;

    /**
     * 利息
     */
    private BigDecimal interest;

    /**
     * 到期时间
     */
    private Date maturityTime;
    /**
     * 还款时间
     */
    private Date paymentTime;
    /**
     * 是否已还  0 已还   1 未还
     */
    private Integer status;
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 是否还
     */
    @TableField(exist = false)
    private String repayMoney;

}
