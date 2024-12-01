package com.ganmashop.controller;

import com.ganmashop.entity.User;
import com.ganmashop.entity.Cart;
import com.ganmashop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 16/11/2024
 */
@Controller
@RequestMapping("/ganma")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String showCartPage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            // Redirect to login if user is not logged in
            redirectAttributes.addFlashAttribute("error", "Please login to add items to your cart.");
            return "redirect:/login";
        }else{
            model.addAttribute("isLoggedIn", true); // 如果user不为空，表示已登录
        }

        List<Cart> cart = cartService.getCartItems("2");
        model.addAttribute("carts", cart);

        if (cart == null) {
            model.addAttribute("error", "Cart not found!"); // 提示未找到
            return "error"; // 指向错误页面 (可选)
        }

        model.addAttribute("cart", cart);

        return "cart";
    }


}
