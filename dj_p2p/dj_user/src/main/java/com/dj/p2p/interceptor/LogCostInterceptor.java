package com.dj.p2p.interceptor;

import com.dj.p2p.common.constant.NumberConstant;
import com.dj.p2p.pojo.token.UserToken;
import com.dj.p2p.service.RedisService;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LogCostInterceptor implements HandlerInterceptor {

    private static final String TOKEN_KEY = "token";


    /**
     * token验证
     * `
     *
     * @param token
     * @return
     */
    private boolean checkToken(String token) {
        if (StringUtils.hasText(token)) {
            // token有效性校验
            if (redisService.checkKeyIsExist(token)) {
                // 验证通过
                redisService.expireKey(token, NumberConstant.REDIS_EXPIRATION_TIME);
                return true;
            }
        }
        return false;
    }

    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 是ajax请求 从Harder中获取Token信息
        String token = request.getHeader(TOKEN_KEY);
        UserToken userToken = redisService.get(token);
        if (checkToken(token)) {
       /*     if (userToken != null && userToken.getRealName().equals(NumberConstant.SIX)) {
                return true;
            } else {
                response.sendRedirect(request.getContextPath() + "/user/realName");//转发到请实名认证
            }*/
            return true;
        } else {
            response.sendRedirect(request.getContextPath() + "/user/login");//转发到未登录
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        System.out.println(modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
