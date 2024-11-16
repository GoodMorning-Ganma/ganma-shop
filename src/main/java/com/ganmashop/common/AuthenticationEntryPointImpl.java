package com.ganmashop.common;

import com.alibaba.fastjson.JSON;
import com.ganmashop.utils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult<String> result = new ResponseResult<>(HttpStatus.UNAUTHORIZED.value(), "认证失败请重新登录");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
        System.out.println("Request: " + request.getRequestURI());
        System.out.println("Error: " + authException.getMessage());
    }
}