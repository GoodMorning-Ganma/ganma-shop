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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
                List<OrderDTO> orderDetails = orderService.getAllOrderDetails();
                model.addAttribute("orderDetails", orderDetails);
                return "admin/index";
            } catch (Exception e) {
                model.addAttribute("error", "Failed to fetch orders. Please try again later.");
                return "admin/index";
            }
        }
    }

    @PostMapping("/orders/{id}/deliver")
    public String updateOrderStatus(Model model, @PathVariable Order order, HttpSession session) {
        // 权限校验
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("isLoggedIn", false);
            return "redirect:/auth/login";
        }

        try {
            orderService.updateOrderStatus(order.getId(), "delivered");
            model.addAttribute("error", "状态更新成功");
            return "admin/index";
        } catch (Exception e) {
            model.addAttribute("error", "状态更新失败");
            return "admin/index";
        }
    }
}
