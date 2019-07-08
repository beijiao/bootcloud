package com.dj.p2p.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("p2p_charging_mode")
public class ChargingMode implements Serializable {


    /**
     * id 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 提前还款违约百分比
     */
    private BigDecimal advance;
    /**
     * 逾期百分比
     */
    private BigDecimal beoverdue;

    /**
     * 逾期罚款
     */
    private BigDecimal beoverduePenalty;
    /**
     * 服务费类型
     */
    private BigDecimal serviceCharge;
    /**
     * 服务费百分比
     */
    private BigDecimal servicePercentage;
    /**
     * 筹款时间
     */
    private Integer fundraisingTime;
    /**
     * 风控建议
     */
    private String windControl;

    /**
     * 发售时间
     */
    private Date salesTime;

    /**
     * 借款合同
     */
    private Integer loanContract;

    /**
     * 标的id
     */
    private Integer markId;

    /**
     * 筹款进度
     */
    private Integer speedProgress;




}
