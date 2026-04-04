package com.ganmashop.config;

import com.ganmashop.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Exposes {@code isAdminUser} for Thymeleaf (e.g. admin-only nav links on the shop site).
 */
@ControllerAdvice(basePackages = "com.ganmashop.controller")
public class GlobalModelAttributesAdvice {

    @ModelAttribute("isAdminUser")
    public boolean isAdminUser(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getUserType() == null) {
            return false;
        }
        return "admin".equalsIgnoreCase(user.getUserType().trim());
    }
}
