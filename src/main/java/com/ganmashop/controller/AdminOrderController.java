package com.ganmashop.controller;

import com.ganmashop.dto.OrderDTO;
import com.ganmashop.entity.Order;
import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import com.ganmashop.service.OrderService;
import com.ganmashop.service.ProductService;
import com.ganmashop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    private UserService userservice;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @GetMapping("/index")
    public String showIndexPage(Model model, HttpSession session) {
        // 登录检查可以根据实际需求保留，或者由 Spring Security 统一管理
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("isLoggedIn", false);
            return "redirect:/auth/login";
        } else {
            model.addAttribute("isLoggedIn", true);
            try {
                List<OrderDTO> recentOrderDetails = orderService.getRecentOrderDetails();
                model.addAttribute("orderDetails", recentOrderDetails);
                return "admin/index";
            } catch (Exception e) {
                model.addAttribute("error", "Failed to fetch orders. Please try again later.");
                return "admin/index";
            }
        }
    }

    @GetMapping("/order")
    public String showOrderPage(Model model, HttpSession session) {
        // 登录检查可以根据实际需求保留，或者由 Spring Security 统一管理
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("isLoggedIn", false);
            return "redirect:/auth/login";
        } else {
            model.addAttribute("isLoggedIn", true);
            try {
                List<OrderDTO> orderDetails = orderService.getAllOrderDetails();
                model.addAttribute("orderDetails", orderDetails);
                return "admin/order";
            } catch (Exception e) {
                model.addAttribute("error", "Failed to fetch orders. Please try again later.");
                return "admin/order";
            }
        }
    }

    @PostMapping("/orders/{id}/deliver")
    @ResponseBody // 告诉Spring返回JSON而不是View
    public ResponseEntity<String> updateOrderStatus(@PathVariable("id") String orderId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        try {
            orderService.updateOrderStatus(orderId, "Delivered");
            return ResponseEntity.ok("状态更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("状态更新失败");
        }
    }
}
