package com.ganmashop.controller;

import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import com.ganmashop.service.ProductService;
import com.ganmashop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Desmondlzk
 * Date: 09/11/2024
 */
@Controller
@RequestMapping("/ganma")
public class IndexController {

    @Autowired
    private ProductService productService;

    @GetMapping("/index")
    public String showIndexPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("isLoggedIn", user != null); // 如果user不为空，表示已登录

        List<Product> products = productService.findAllProducts(); // Assume this returns all products
        model.addAttribute("products", products);

        return "index";
    }
    @GetMapping("/account")
    public String showAccountPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("isLoggedIn", user != null); // 如果user不为空，表示已登录

        return "account"; // Refers to the account.html page in the templates folder
    }
}
