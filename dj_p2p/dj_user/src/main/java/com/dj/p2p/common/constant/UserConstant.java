package com.dj.p2p.common.constant;

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
    /*-----------角色---------*/
    /**
     * 投资人
     */
    Integer ROLE_INVESTOR = 39;
    /**
     * 借款人
     */
    Integer ROLE_BORROWER = 38;

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
     * 风控总监
     */
    String ROLE_DIRECTOR_OF_WIND_CONTROL_STRING = "ROLE_DIRECTOR_OF_WIND_CONTROL";

    /**
     * 投资人
     */
    /*    Integer ROLE_INVESTOR = 39;
     */
    /**
     * 借款人
     *//*
    Integer ROLE_BORROWER = 38;*/
    Integer IS_WIND = 1;
    /**
     * 风控专员
     */
    String ROLE_WIND_CONTROL_COMMISSIONER_STRING = "ROLE_WIND_CONTROL_COMMISSIONER";
    /**
     * 风控经理
     */
    String ROLE_WIND_CONTROL_MANAGER_STRING = "ROLE_WIND_CONTROL_MANAGER";
    /**
     * 用户被锁
     */
    Integer LOCK = 1;

    /**
     * 用户解锁
     */
    Integer UNLOCKED = 0;
    String CURRENTLY_UNLOCKED = "当前为解锁，无需再次解锁";
    String CURRENTLY_LOCKED = "当前为锁定，无需再次锁定";

    String CURRENTLY_LOCKED_ONE = "用户已锁定";
    /**
     * 没还
     */
    Integer NO_REPAYMENT = 1;
    String NO_REPAYMENT_STRING = "待还";
    /**
     * 已还
     */
    Integer REPAYMENT = 0;
    String REPAYMENT_STRING = "已还";

    /**
     * 没签约
     */
    Integer NOT_SIGNING = 1;
    Integer SIGNING = 0;
}
