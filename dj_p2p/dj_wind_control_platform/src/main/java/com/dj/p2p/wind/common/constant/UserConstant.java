package com.dj.p2p.wind.common.constant;

/**
 * 用户常量类
 */
public interface UserConstant {


    /**
     * 充值多少钱
     */
    String UP_MONEY = "upMoney";
    /**
     * 最低充值金额
     */
    double LOWEST_MONEY = 10.00;

    /*-----------------角色-------------------------*/

    /**
     * 风控专员
     */
    Integer ROLE_WIND_CONTROL_COMMISSIONER = 40;

    /**
     * 风控经理
     */
    Integer ROLE_WIND_CONTROL_MANAGER = 41;

    /**
     * 风控总监
     */
    Integer ROLE_DIRECTOR_OF_WIND_CONTROL = 42;

    /**
     * 投资人
     */
    Integer ROLE_INVESTOR = 39;
    /**
     * 借款人
     */
    Integer ROLE_BORROWER = 38;
    Integer IS_WIND = 1;
}
