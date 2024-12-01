package com.ganmashop.controller;

import com.ganmashop.entity.User;
import com.ganmashop.service.UserService;
import com.ganmashop.utils.BusinessException;
import com.ganmashop.utils.SecurityContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Desmondlzk
 * Date: 09/11/2024
 */
@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());

        // 展示登录界面，login.html
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute User user, Model model, HttpServletRequest request, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            SecurityContextUtil.saveSecurityContextToSession(request, authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User foundUser = userService.findByUsername(user.getUsername());
            session.setAttribute("loggedInUser", foundUser);

            switch (foundUser.getUserType()) {
                case "admin":
                    return "redirect:/admin/";
                case "customer":
                    return "redirect:/ganma/index";
                default:
                    model.addAttribute("error", "Invalid role");
                    return "login";
            }



        } catch (AuthenticationException e) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 清空会话
        return "redirect:/ganma/index"; // 登出后重定向到首页
    }
}
