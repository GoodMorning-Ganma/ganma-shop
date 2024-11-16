package com.ganmashop.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * @author Desmondlzk
 * Date: 20/10/2024
 */
public class SecurityContextUtil {

    // 存储 SecurityContext 到会话中
    public static void saveSecurityContextToSession(HttpServletRequest request, Authentication authentication) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    // 从会话中提取 SecurityContext
    public static SecurityContext getSecurityContextFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 如果会话不存在，则返回 null
        if (session != null) {
            return (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        }
        return null;
    }
}