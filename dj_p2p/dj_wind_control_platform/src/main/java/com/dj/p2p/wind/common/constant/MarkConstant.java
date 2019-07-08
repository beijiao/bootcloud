package com.dj.p2p.wind.common.constant;

/**
 * 标常量类
 */
public interface MarkConstant {

    /**
     * 初审中
     */
    Integer IN_THE_FIRST_INSTANCE = 68;
    String IN_THE_FIRST_INSTANCE_STRING = "初审中";
    /**
     * 复审中
     */
    Integer REVIEW_IN_PROGRESS = 69;
    String REVIEW_IN_PROGRESS_STRING = "复审中";
    /**
     * 完成
     */
    Integer COMPLETE = 80;
    /**
     * 拒绝
     */
    Integer REVIEW_IN_PROGRESS_REFUSE = 86;
    Integer IN_THE_FIRST_INSTANCE_REFUSE = 85;

    /**
     * 同意
     */
    String AGREE = "同意";
    String REFUSE_STRING = "拒绝";

    /**
     * 收手续费方式
     */
    Integer CHARGE_TYPE = 75;
    String SUCCESS = "完成";
    /**
     * 签约
     */
    Integer SIGNING = 0;


    /*
    *   79	筹款状态	0	0
        80	借款中	79	0
        81	待放款	79	0
        82	还款中	79	0
        83	已完成	79	0
        84	流标	79	0
    *
    *
    * */

}
