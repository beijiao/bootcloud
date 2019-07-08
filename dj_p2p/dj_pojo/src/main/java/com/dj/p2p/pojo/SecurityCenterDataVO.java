package com.dj.p2p.pojo;

import lombok.Data;

/**
 * 安全中心页面需要的数据
 */
@Data
public class SecurityCenterDataVO {

    /**
     * 开户状态
     */
    private Boolean openAccountStatus = false;

    /**
     * 陕坝银行开户账号
     */
    private String openAccountCode;

    /**
     * 是否实名认证
     */
    private Boolean isReal = false;

    /**
     * 身份证号
     */
    private String idCode;
    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 是否绑定手机
     */
    private Boolean isBindingPhone = false;

    /**
     * 绑定的手机号
     */
    private String bindingPhone;
    /**
     * 是否绑定银行卡
     */
    private Boolean isBindingBankCard = false;

    /**
     * 绑定的银行卡号
     */
    private String bandingBankCard;


}
