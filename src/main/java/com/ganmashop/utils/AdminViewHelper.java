package com.ganmashop.utils;

import com.ganmashop.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public final class AdminViewHelper {

    private AdminViewHelper() {
    }

    public static void putWelcomeNameIfLoggedIn(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            String display = (user.getName() != null && !user.getName().isBlank())
                    ? user.getName().trim()
                    : user.getUsername();
            model.addAttribute("welcomeName", display);
        }
    }
}
