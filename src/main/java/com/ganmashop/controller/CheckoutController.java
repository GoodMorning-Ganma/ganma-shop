package com.ganmashop.controller;

import com.ganmashop.dto.CartProductDTO;
import com.ganmashop.dto.OrderDTO;
import com.ganmashop.entity.Cart;
import com.ganmashop.entity.Order;
import com.ganmashop.entity.User;
import com.ganmashop.service.CartService;
import com.ganmashop.service.OrderService;
import com.ganmashop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/ganma")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    // -----------------------------
    // SHOW CHECKOUT PAGE
    // -----------------------------
    @GetMapping("/checkout")
    public String showCheckoutPage(
            @RequestParam("productIds") List<String> productIds,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/auth/login";

        List<Cart> cartItems = cartService.getCartItems(user.getId());

        List<CartProductDTO> selectedItems = cartItems.stream()
                .filter(c -> productIds.contains(c.getProductId()))
                .map(c -> new CartProductDTO(c, productService.findProductById(c.getProductId())))
                .toList();

        BigDecimal subtotal = selectedItems.stream()
                .map(i -> i.getCart().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("user", user);
        model.addAttribute("orderItems", selectedItems);
        model.addAttribute("productIds", productIds);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("deliveryFee", 2.00);
        model.addAttribute("tax", 0);
        model.addAttribute("total", subtotal .add(BigDecimal.valueOf(2)));
        model.addAttribute("isLoggedIn", true);

        return "checkout";
    }

    // -----------------------------
    // CONFIRM ORDER (CREATE MULTIPLE ORDERS)
    // -----------------------------
    @PostMapping("/checkout/submit")
    public String submitOrder(
            @RequestParam List<String> productIds,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/auth/login";

        List<Cart> cartItems = cartService.getCartItems(user.getId());
        List<String> orderIds = new ArrayList<>();

        for (Cart c : cartItems) {
            if (productIds.contains(c.getProductId())) {
                // 检查是否已有 Pending 订单
                Order pendingOrder = orderService.getPendingOrderByUserAndProduct(user.getId(), c.getProductId());

                String orderId;
                if (pendingOrder != null) {
                    // 已有 Pending 订单，复用它的 orderId
                    orderId = pendingOrder.getId();
                } else {
                    // 没有 Pending 订单，创建新的
                    orderId = orderService.createOrder(
                            user.getId(),
                            c.getProductId(),
                            c.getQuantity(),
                            c.getPrice() .add(BigDecimal.valueOf(2)),
                            "Pending"
                    );
                }

                orderIds.add(orderId);
            }
        }

        // 删除购物车中已下单商品
        cartService.deleteSelectedItems(user.getId(), productIds);

        if (orderIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Order error occurred.");
            return "redirect:/ganma/checkout";
        }

        // 保存到 Session，方便 payment 页面使用
        session.setAttribute("pendingOrderIds", orderIds);

        return "redirect:/ganma/payment";
    }


    @PostMapping("/orderPayment/pending")
    public String savePendingOrder(
            @RequestParam List<String> productIds,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        List<Cart> cartItems = cartService.getCartItems(user.getId());

        for (Cart c : cartItems) {
            if (productIds.contains(c.getProductId())) {

                // ✅ 每次都创建新的 Pending 订单（不要复用旧的）
                orderService.createOrder(
                        user.getId(),
                        c.getProductId(),
                        c.getQuantity(),
                        c.getPrice(),
                        "Pending"
                );
            }
        }

        redirectAttributes.addFlashAttribute("success", "Order saved as pending. You can complete payment later.");
        return "redirect:/ganma/orderPayment";
    }


    // -----------------------------
    // SHOW PAYMENT PAGE (MULTI ORDERS)
    // -----------------------------
    @GetMapping("/payment")
    public String showPaymentPage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/auth/login";

        List<String> orderIds = (List<String>) session.getAttribute("pendingOrderIds");
        if (orderIds == null || orderIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No pending orders found.");
            return "redirect:/ganma/cart";
        }

        List<OrderDTO> orders = orderIds.stream()
                .map(orderService::getOrderDetailsById)
                .toList();

        BigDecimal total = orders.stream()
                .map(o -> o.getOrder().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("orders", orders);
        model.addAttribute("total", total);
        model.addAttribute("user", user);

        return "payment";
    }


    // -----------------------------
    // PAYMENT SUCCESS (UPDATE ALL)
    // -----------------------------
    @PostMapping("/order/payment/success")
    public String paymentSuccess(
            @RequestParam("orderIds") List<String> orderIds,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null){
            return "redirect:/auth/login";
        }

        for (String orderId : orderIds) {
            orderService.updateOrderStatus(orderId, "Paid");
        }

        session.removeAttribute("pendingOrderIds");

        redirectAttributes.addFlashAttribute("success", "Payment successful!");
        return "redirect:/ganma/order";
    }
}
