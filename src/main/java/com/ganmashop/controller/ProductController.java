package com.ganmashop.controller;

import com.ganmashop.entity.Cart;
import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import com.ganmashop.service.CartService;
import com.ganmashop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jasonlzc
 * Date: 18/11/2024
 */
@Controller
@RequestMapping("/ganma")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @GetMapping("/product/{id}")
    public String showProductDetail(HttpSession session, @PathVariable("id") String id, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("isLoggedIn", user != null); // 如果user不为空，表示已登录

        Product product = productService.findProductId(id);


        if (product == null) {
            model.addAttribute("error", "Product not found!"); // 提示未找到
            return "error"; // 指向错误页面 (可选)
        }

        model.addAttribute("product", product);
        return "product"; // Refers to the product.html page in the templates folder
    }
    @GetMapping("/add")
    public String addToCartViaGet(@ModelAttribute Cart cart,
                                  HttpSession session) {
        String userId = (String) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/auth/login"; // Redirect if not logged in
        }
        cartService.addToCart(cart);
        return "redirect:/cart"; // Redirect to the cart page
    }

}
