package com.dj.p2p.wind.common;

public enum MarkEnum {

/*     80	借款中	79	0
            81	待放款	79	0
            82	还款中	79	0
            83	已完成	79	0
            84	流标	79	0*/

    /**
     * 待放款
     */
    TO_BE_LENT(81, "待放款"),

    BORROWING(80, "借款中"),
    /**
     * 还款中
     */
    AMONG_REPAYMENTS(82, "还款中"),
    /**
     * 已完成
     */
    COMPLETED(83, "已完成"),
    /**
     * 流标
     */
    FAIL_TO_BE_SOLD_AT_AUCTION(84, "流标"),

    THREE(58,"3期"),
    SIX(59,"6期"),
    NINE(60,"9期"),
    TWELVE(61,"12期");
    private Integer id;
    private String name;

    MarkEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
