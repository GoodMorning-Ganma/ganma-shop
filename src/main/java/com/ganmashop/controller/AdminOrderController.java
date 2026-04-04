package com.ganmashop.controller;

import com.ganmashop.dto.OrderDTO;
import com.ganmashop.entity.Order;
import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import com.ganmashop.service.OrderService;
import com.ganmashop.service.ProductService;
import com.ganmashop.service.UserService;
import com.ganmashop.utils.AdminViewHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
            AdminViewHelper.putWelcomeNameIfLoggedIn(session, model);
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
            AdminViewHelper.putWelcomeNameIfLoggedIn(session, model);
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

    /**
     * 轮询：统计创建时间晚于指定时间点的订单数量（用于管理端新订单提醒）。
     */
    @GetMapping("/api/orders/new-count")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> countNewOrdersSince(
            @RequestParam("after") long afterEpochMs,
            HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        int count = orderService.countOrdersCreatedAfter(new Date(afterEpochMs));
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 用于管理端通知：列出创建时间晚于指定时间点的订单 id（客户端再结合本地「单条已读」集合计算角标）。
     */
    @GetMapping("/api/orders/new-ids-since")
    @ResponseBody
    public ResponseEntity<Map<String, List<String>>> listNewOrderIdsSince(
            @RequestParam("after") long afterEpochMs,
            HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<String> ids = orderService.findOrderIdsCreatedAfter(new Date(afterEpochMs));
        Map<String, List<String>> body = new HashMap<>();
        body.put("ids", ids);
        return ResponseEntity.ok(body);
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
