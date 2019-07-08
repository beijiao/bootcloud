package com.dj.p2p.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 风控平台用户信息
 */
@Data
public class UserInfo implements Serializable {


    /**
     * 编号
     */
    private Integer number;

    private Integer id;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 最近登录时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date lastTime;

    /**
     * 登录次数
     */
    private Integer loginCount;
    /**
     * 开户状态
     */
    private String openingStatus;
    /**
     * 状态
     */
    private String status;


}
