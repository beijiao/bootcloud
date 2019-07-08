package com.dj.p2p.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * 账户表实体
 *
 * @author yyw
 */
@Data
@TableName("p2p_account")
public class Account implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 证件号码
     */

    private String number;

    /**
     * 银行卡号
     */
    private String cardNo;

    /**
     * 账户类型
     */
    private Integer accountType;

    /**
     * 银行预留手机号
     */
    private String reservedPhone;
    /**
     * 交易密码
     */
    private String transPassword;

    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 虚拟账号
     */
    private String virtualAccount;


}
