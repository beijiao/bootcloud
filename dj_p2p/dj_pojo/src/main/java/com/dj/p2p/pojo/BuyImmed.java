package com.dj.p2p.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BuyImmed implements Serializable {

    /**
     * 目前筹款金额
     */
    private BigDecimal currentMoney;
    /**
     * 标的价钱
     */
    private BigDecimal money;
    /**
     * 单人投资是否限额
     */
    private Integer isQuota;

    /**
     * 限额百分比
     */
    private  BigDecimal quotaCount;


    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 最大可投
     */
    private BigDecimal maxQuota;
    /**
     * 筹款进度
     */
    private BigDecimal progress;
}
