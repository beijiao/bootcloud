package com.dj.p2p.common.constant;

import java.math.BigDecimal;

public interface NumberConstant {

    /**
     * redis过期时间
     */
    Integer REDIS_EXPIRATION_TIME = 3600;
    /**
     * 错误状态码 -2
     */
    Integer ERROR_CODE_TWO = -2;
    /**
     * 错误码
     */
    Integer ERROR_CODE_TWO_BIG = 2;
    /**
     * 数字6
     */
    int SIX = 6;
    int FOURTEEN = 14;
    int ZERO = 0;
    int THREE = 3;
    int NINE = 9;
    int FIFTEEN = 15;
    int SEVEN = 7;
    int FOUR = 4;
    int SIXTEEN = 16;
    BigDecimal BIGDECIMEL_ZERO = new BigDecimal(0.00);
    Integer ONE = 1;
    BigDecimal ONE_HUNDRED = new BigDecimal(100);
}
