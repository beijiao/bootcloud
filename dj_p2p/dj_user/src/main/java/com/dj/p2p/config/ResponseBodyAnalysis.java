package com.dj.p2p.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseBodyAnalysis implements ResponseBodyAdvice {

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter arg1,
                                  MediaType arg2, Class arg3, ServerHttpRequest arg4,
                                  ServerHttpResponse arg5) {
        try {
            Class<?> aClass = body.getClass();
            Object o = aClass.newInstance();
            System.out.println(o.toString()+"获取了++++++++++++++++++++++++++");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    @Override
    public boolean supports(MethodParameter arg0, Class arg1) {
        return true;
    }

}