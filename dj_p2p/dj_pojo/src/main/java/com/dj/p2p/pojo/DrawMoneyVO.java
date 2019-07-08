package com.dj.p2p.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DrawMoneyVO implements Serializable {

    /**
     * 当前可用余额
     */
    private BigDecimal availableBalance;

    /**
     * 提现银行卡号
     */
    private String bankCode;

    /**
     * 提现方式
     */
    private String drawMoneyType;


}
