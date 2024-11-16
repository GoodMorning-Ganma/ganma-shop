package com.ganmashop.controller;

import com.ganmashop.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Desmondlzk
 * Date: 09/11/2024
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/")
    public String showIndexPage(Model model) {
        model.addAttribute("user", new User());
        System.out.println("Login successful");

        // 展示admin主页（index.html)
        return "admin/index";
    }
}
