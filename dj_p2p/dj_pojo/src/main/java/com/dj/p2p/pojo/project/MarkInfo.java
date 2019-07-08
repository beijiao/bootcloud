package com.dj.p2p.pojo.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class MarkInfo implements Serializable {


    private Integer id;

    private String projectName;

    private BigDecimal money;

    private BigDecimal yearInterestRate;

    private String term;

    /**
     * 发起时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GTM+8")
    private Date createTime;

    /**
     * 加入人数
     */
    private Integer personCount;

}
