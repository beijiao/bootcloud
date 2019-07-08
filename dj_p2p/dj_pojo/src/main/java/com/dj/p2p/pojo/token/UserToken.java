package com.dj.p2p.pojo.token;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserToken implements Serializable {


    /**
     * 用户id
     */
    private Integer id;

    /**
     * token
     */
    private String token;

    /**
     * 用户角色
     */
    private  String role;

    /**
     * 用户真实姓名
     */
    private String realName;
}
