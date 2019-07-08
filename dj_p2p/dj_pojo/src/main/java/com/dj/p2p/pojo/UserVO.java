package com.dj.p2p.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    /**
     * 编号
     */
    private Integer number;

    /**
     * 身份证号
     */

    private String idNumber;

    /**
     * 真实姓名
     */
    private String realName;


}
