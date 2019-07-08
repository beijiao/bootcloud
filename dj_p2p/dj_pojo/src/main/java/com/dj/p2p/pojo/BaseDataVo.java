package com.dj.p2p.pojo;

import lombok.Data;

@Data
public class BaseDataVo {

    private Integer id;
    /**
     * 名称
     */

    private String baseName;
    /**
     * 父id
     */

    private Integer pId;

}
