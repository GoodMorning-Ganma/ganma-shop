package com.ganmashop.controller;

import com.ganmashop.dto.OrderDTO;
import com.ganmashop.entity.Order;
import com.ganmashop.entity.User;
import com.ganmashop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

            List<Order> orders = orderService.getPendingOrdersByUserId(user.getId());
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

    @GetMapping("/order/details/{orderId}")
    @ResponseBody
    public OrderDTO getOrderDetails(@PathVariable String orderId) {
        return orderService.getOrderDetailsById(orderId);
    }

    @GetMapping("/orderPayment")
    public String showOrderPayment(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You are required to login.");
            return "redirect:/auth/login";
        }

        model.addAttribute("isLoggedIn", true);

        List<Order> pendingPaymentOrders = orderService.getPendingPaymentOrders(user.getId());

        model.addAttribute("orders", pendingPaymentOrders);

        return "orderPayment";
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
            List<Order> orders = orderService.getDeliveredOrdersByUserId(user.getId());
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