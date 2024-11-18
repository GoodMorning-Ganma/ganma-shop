package com.ganmashop.controller;

import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import com.ganmashop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jasonlzc
 * Date: 18/11/2024
 */
@Controller
@RequestMapping("/ganma")
public class ProductController {

    @Autowired
    private ProductService productService;

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
}
