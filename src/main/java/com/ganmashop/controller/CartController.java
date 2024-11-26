package com.ganmashop.controller;

import com.ganmashop.entity.User;
import com.ganmashop.entity.Cart;
import com.ganmashop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 16/11/2024
 */
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String showCartPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("isLoggedIn", user != null); // 如果user不为空，表示已登录

        return "cart";
    }


}
