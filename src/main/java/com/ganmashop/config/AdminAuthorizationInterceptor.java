package com.ganmashop.config;

import com.ganmashop.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Ensures only admin accounts can access /admin/** routes (session-based shop login).
 */
@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("loggedInUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return false;
        }
        String type = user.getUserType();
        if (type == null || !"admin".equalsIgnoreCase(type.trim())) {
            response.sendRedirect(request.getContextPath() + "/ganma/index");
            return false;
        }
        return true;
    }
}
