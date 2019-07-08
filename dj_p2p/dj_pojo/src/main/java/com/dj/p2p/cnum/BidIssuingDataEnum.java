package com.dj.p2p.cnum;

import lombok.Getter;

/**
 * @author yyw
 * 发标页面需要的数据
 */
@Getter
public enum BidIssuingDataEnum {


    PRODUCT(43, "product", 0),

    REAL_ESTATE_MORTGAGE(44, "房产抵押", 43),

    VEHICLE_MORTGAGE(45, "车辆抵押", 43),

    CREDIT_LOAN(46, "信用借款", 43),

    BUSINESS_BORROWINGS(47, "企业借款", 43),

    MARK(48, "mark", 0),
    YES_DISPLY(49, "显示", 48),
    NOT_DISPLY(50, "不显示", 48),
    MARK_TYPE(51, "mark_type", 0),
    INDIVIDUALTARGET(52, "个人标", 51),
    ENTERPRISE_STANDARD(53, "企业标", 51),
    SINGLE_LIMIT(54, "single_limit", 0),
    OPEN_QUOTA(55, "不限额", 54),
    QUOTA(56, "限额", 54),
    TERM(57, "term", 0),

    THE_THREE_PHASE(58, "3期", 57),
    THE_SIX_PHASE(59, "6期", 57),
    THE_NINE_PHASE(60, "9期", 57),
    THE_TWELVE_PHASE(61, "12期", 57),
    PAYMENT_METHOD(62, "PAYMENT_METHOD", 0),
    EQUIVALENT_PRINCIPAL_AND_INTEREST(63, "等额本息", 62),
    FIRST_INTEREST_AFTER_PRINCIPAL(64, "先息后本", 62),
    EQUIVALENT_PRINCIPAL(65, "等额本金", 62),
    PAYMENT_DUE(66, "到期还清", 62);

    private Integer id;
    private String name;
    private Integer pId;

    BidIssuingDataEnum(Integer id, String baseName, Integer pId) {
        this.id = id;
        this.pId = pId;
        this.name = baseName;

    }
}
