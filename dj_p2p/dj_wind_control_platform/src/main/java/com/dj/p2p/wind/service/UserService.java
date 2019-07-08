package com.dj.p2p.wind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dj.p2p.base.BusinessException;
import com.dj.p2p.pojo.User;
import com.dj.p2p.pojo.token.UserToken;

public interface UserService extends IService<User> {


    /**
     * 风控平台用户登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    UserToken tokenLogin(String phone, String password) throws Exception, BusinessException;

    /**
     * 风控平台注册
     *
     * @param phone    手机号
     * @param password 密码
     * @throws Exception
     */
    void addWindRegister(String phone, String password) throws Exception, BusinessException;


}
