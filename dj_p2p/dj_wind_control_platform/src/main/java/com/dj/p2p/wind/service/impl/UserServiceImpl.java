package com.dj.p2p.wind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.base.BusinessException;
import com.dj.p2p.pojo.User;
import com.dj.p2p.pojo.token.UserToken;
import com.dj.p2p.wind.common.UUIDUtil;
import com.dj.p2p.wind.common.constant.MSGConstant;
import com.dj.p2p.wind.common.constant.NumberConstant;
import com.dj.p2p.wind.common.constant.UserConstant;
import com.dj.p2p.wind.mapper.UserMapper;
import com.dj.p2p.wind.service.RedisService;
import com.dj.p2p.wind.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private RedisService redisService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 风控平台用户登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    @Override
    public UserToken tokenLogin(String phone, String password) throws Exception, BusinessException {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.eq("password", password);
        User user = this.getOne(queryWrapper);
        if (user != null && user.getRole() != null) {
            if (user.getRole().equals(UserConstant.ROLE_DIRECTOR_OF_WIND_CONTROL) || user.getRole().equals(UserConstant.ROLE_WIND_CONTROL_COMMISSIONER) || user.getRole().equals(UserConstant.ROLE_WIND_CONTROL_MANAGER)) {
                String token = UUIDUtil.getUUID();
                UserToken userToken = new UserToken();
                userToken.setToken(token);
                userToken.setId(user.getId());
                redisService.set(token, userToken, NumberConstant.REDIS_EXPIRATION_TIME);
                return userToken;
            } else {
                throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.ERROR_INCORRECT_USERNAME_OR_PASSWORD);
            }
        }
        throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.ERROR_INCORRECT_USERNAME_OR_PASSWORD);
    }


    /**
     * 风控平台注册
     *
     * @param phone    手机号
     * @param password 密码
     * @throws Exception
     */
    @Override
    public void addWindRegister(String phone, String password) throws Exception, BusinessException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User user = this.getOne(queryWrapper);
        if (user != null) {
            throw new BusinessException(NumberConstant.ERROR_CODE_TWO, MSGConstant.MOBILE_NUMBER_ALREADY_EXISTS);
        }
        user.setRole(UserConstant.ROLE_WIND_CONTROL_COMMISSIONER);
        user.setPassword(password);
        user.setPhone(phone);
        this.save(user);
    }


}
