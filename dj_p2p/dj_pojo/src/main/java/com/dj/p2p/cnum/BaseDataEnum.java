package com.dj.p2p.cnum;

import lombok.Getter;

/**
 * 基础数据枚举类
 *
 * @author yyw
 */
@Getter
public enum BaseDataEnum {

    /**
     * 性别
     */
    SEX(1, "sex", 0),
    MAN(12, "男", 1),
    WOMAN(5, "女", 1),
    SECRECY(15, "保密", 1),
    MARRIAGE(2, "marriage", 0),
    MARRIED(13, "已婚", 2),
    DISCOVERTURE(14, "未婚", 2),
    EDU(3, "education", 0),
    EDU_SMALL_SCHOOL(6, "小学", 3),
    EDU_ZHONG_SCHOOL(7, "初中", 3),
    EDU_HEIGHT_SCHOOL(8, "高中", 3),
    EDU_ZHUAN_SCHOOL(9, "专科", 3),
    EDU_BEN_SCHOOL(10, "本科", 3),
    EDU_GRADUATE_SCHOOL(11, "研究生及以上", 3),
    WORKING_COUNT(4, "working_life", 0),
    WORKING_ONE_THREE(16, "1-3年", 4),
    WORKING_THREE_FIVE(17, "3-5年", 4),
    WORKING_FIVE_EIGHT(18, "5-8年", 4),
    WORKING_BIG_EIGHT(19, "8年以上", 4),

    HOUSE(20, "house_property", 0),
    HOUSE_QUAN(21, "全款买房", 20),
    HOUSE_DAI(22, "贷款买房", 20),
    HOUSE_RENTING(23, "租房", 20),
    HOUSE_NOTHAVE(24, "无", 20),
    YEAR_ANNUAL(25, "annual_income", 0),
    YEAR_ONE_FIVE(26, "1~5万", 25),
    YEAR_FIVE_TEN(27, "5~10万", 25),
    YEAR_TEN_TWENTY(28, "10~20万", 25),
    YEAR_TWENTY_THIRTY(29, "20~30万", 25),
    YEAR_THIRTY_FIFTY(30, "30~50万", 25),
    YEAR_FIFTY_HUNDRED(31, "50万~100万", 25),
    YEAR_BIG_HUNDRED(32, "100万以上", 25),
    CAR(33, "vehicle_production", 0),
    CAR_TOTAL(34, "全款买车", 33),
    CAR_LOAN(35, "贷款买车", 33),
    CAR_NOTHAVE(36, "无", 33),
    ROLE(37, "role", 0),
    ROLE_BORROWER(38, "借款人", 37),
    ROLE_INVESTOR(39, "投资人", 37),
    ROLE_CONTROL_SPECIALIST(40, "风控专员", 37),
    ROLE_MANAGER(41, "风控经理", 37),
    ROLE_MAJORDOMO(42, "风控总监", 37);


    private Integer id;
    private String baseName;
    private Integer pId;

    BaseDataEnum(Integer id, String name, Integer pId) {
        this.id = id;
        this.baseName = name;
        this.pId = pId;
    }


}
