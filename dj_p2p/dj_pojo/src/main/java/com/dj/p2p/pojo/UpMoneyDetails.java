package com.dj.p2p.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UpMoneyDetails implements Serializable {

    /**
     * 客户姓名
     */
    private String name;
    /**
     * 银行卡号
     */
    private String bankCode;

    /**
     * 银行预留手机号
     */
    private String phone;

    /**
     * 充值金额
     */
    private BigDecimal upMoney;


}
