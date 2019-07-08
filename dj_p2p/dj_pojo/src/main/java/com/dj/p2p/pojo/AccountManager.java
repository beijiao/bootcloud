package com.dj.p2p.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yyw
 *
 */
@Data
@TableName("p2p_account_manager")
public class AccountManager implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 账户id
     */
    private Integer accId;

    /**
     * 总收益
     */
    private BigDecimal totalRevenue;
    /**
     * 可用余额
     */
    private BigDecimal availableBalance;
    /**
     * 总资产
     */
    private BigDecimal totalAsset;

    /**
     * 冻结金额
     */
    private BigDecimal freezeMoney;

    /**
     * 待收金额
     */
    private BigDecimal dueMoney;
    /**
     * 待还金额
     */
    private BigDecimal waitRepay;

    /**
     * 交易密码
     */
    @TableField(exist = false)
    private String transPassword;
    /**
     * 用户角色
     */
    @TableField(exist = false)
    private String role;


    /**
     * 用户id
     */
    private Integer userId;
}
