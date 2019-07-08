package com.dj.p2p.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author yyw
 */
@Data
@TableName("p2p_user")
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 密码
     */
    private String password;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 学历
     */
    private Integer education;
    /**
     * 婚姻状态
     */
    private Integer marriage;

    /**
     * 工作年限
     */
    private Integer workingCount;


    /**
     * 房产
     */

    private Integer house;

    private String salt;
    /**
     * 年收入
     */
    private Integer yearIncome;


    /**
     * 是否锁定 0 未锁定 1 锁定
     */
    private Integer status;
    /**
     * 资产估值
     */
    private String valuation;

    /**
     * 车产
     */
    private Integer car;


    /**
     * 身份证号
     */
    private String number;
    /**
     * 真实姓名
     */
    private String name;

    /**
     * 角色
     */
    private Integer role;

    /**
     *
     */
    @TableField(exist = false)
    private Integer isWho;
}
