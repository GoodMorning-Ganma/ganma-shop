package com.ganmashop.controller;

import com.ganmashop.entity.Order;
import com.ganmashop.entity.User;
import com.ganmashop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

/**
 * @author Jasonlzc
 * Date: 30/12/2024
 */
@Controller
@RequestMapping("/ganma")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @GetMapping("/order")
    public String showOrderPage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (Objects.isNull(user)) {
            redirectAttributes.addFlashAttribute("error", "You are required to login.");
            return "redirect:/auth/login";
        }
        model.addAttribute("isLoggedIn", true);
        try {
            List<Order> orders = orderService.getOrdersByUserId(user.getId());
            if (orders == null || orders.isEmpty()) {
                model.addAttribute("message", "No orders found.");
            } else {
                model.addAttribute("orders", orders);
            }
            return "order";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to fetch orders. Please try again later.");
            return "order";
        }
    }

    @GetMapping("/orderHistory")
    public String showOrderHistory(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (Objects.isNull(user)) {
            redirectAttributes.addFlashAttribute("error", "You are required to login.");
            return "redirect:/auth/login";
        }
        model.addAttribute("isLoggedIn", true);
        try {
            List<Order> orders = orderService.getOrdersByUserId(user.getId());
            if (orders == null || orders.isEmpty()) {
                model.addAttribute("message", "No orders found.");
            } else {
                model.addAttribute("orders", orders);
            }
            return "orderHistory";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to fetch orders. Please try again later.");
            return "orderHistory";
        }
    }
}