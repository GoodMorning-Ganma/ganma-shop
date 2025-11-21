package com.ganmashop.controller;

import com.ganmashop.dto.OrderDTO;
import com.ganmashop.entity.Cart;
import com.ganmashop.entity.Order;
import com.ganmashop.entity.User;
import com.ganmashop.service.CartService;
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
    @Autowired
    private CartService cartService;


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

    @PostMapping("/order/reorder/{orderId}")
    public String reorder(
            @PathVariable String orderId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You need to login first.");
            return "redirect:/auth/login";
        }

        try {
            // Get order details
            OrderDTO orderDTO = orderService.getOrderDetailsById(orderId);
            if (orderDTO == null) {
                redirectAttributes.addFlashAttribute("error", "Order not found.");
                return "redirect:/ganma/orderHistory";
            }

            // Add product to cart
            Cart cart = new Cart();
            cart.setUserId(user.getId());
            cart.setProductId(orderDTO.getProduct().getId());
            cart.setQuantity(orderDTO.getOrder().getQuantity());
            cart.setPrice(orderDTO.getProduct().getPrice() * orderDTO.getOrder().getQuantity());

            cartService.save(cart);

            redirectAttributes.addFlashAttribute("success", "Order added to cart successfully.");
            return "redirect:/ganma/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to reorder: " + e.getMessage());
            return "redirect:/ganma/orderHistory";
        }
    }

}