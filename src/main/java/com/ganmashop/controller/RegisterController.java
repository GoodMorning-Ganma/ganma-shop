package com.ganmashop.controller;

import com.ganmashop.entity.User;
import com.ganmashop.service.UserService;
import com.ganmashop.utils.BusinessException;
import com.ganmashop.utils.SecurityContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class RegisterController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());

        // 展示登录界面，login.html
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user, Model model, HttpServletRequest request) {
        try {
            userService.save(user);
            return "redirect:/auth/login";

        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

}
