package com.ganmashop.jwt;

import com.ganmashop.entity.LoginUser;
import com.ganmashop.utils.RedisCache;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Desmondlzk
 * Date: 06/02/2024 - 8:06 PM
 */
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (!StringUtils.hasText(header)) {
            filterChain.doFilter(request, response);
            return;
        }

        if(header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String userid;
            try {
                Claims claims = JwtUtils.parseJWT(token);
                userid = claims.getSubject();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("token非法");
            }

            // 从redis中获取用户信息
            String redisKey = "LoginUser: " + userid;
            LoginUser loginUser = redisCache.getCacheObject(redisKey);
            if (Objects.isNull(loginUser)) {
                throw new RuntimeException("用户未登录");
            }

            // 获取权限信息封装到SecurityContextHolder
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } else {
            throw new RuntimeException("令牌没有以Bearer字符串开头");
        }
    }
}