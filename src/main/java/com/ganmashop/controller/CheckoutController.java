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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ganma")
public class CheckoutController {

    private static final String SESSION_CHECKOUT_NAME = "checkoutDeliveryName";
    private static final String SESSION_CHECKOUT_PHONE = "checkoutDeliveryPhone";
    private static final String SESSION_CHECKOUT_ADDRESS = "checkoutDeliveryAddress";

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

        double subtotal = selectedItems.stream()
                .mapToDouble(i -> i.getCart().getPrice() * i.getCart().getQuantity())
                .sum();

        model.addAttribute("user", user);
        model.addAttribute("orderItems", selectedItems);
        model.addAttribute("productIds", productIds);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("deliveryFee", 2.00);
        model.addAttribute("tax", 0);
        model.addAttribute("total", subtotal + 2);
        model.addAttribute("isLoggedIn", true);

        return "checkout";
    }

    // -----------------------------
    // CONFIRM ORDER (CREATE MULTIPLE ORDERS)
    // -----------------------------
    @PostMapping("/checkout/submit")
    public String submitOrder(
            @RequestParam List<String> productIds,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "paymentMethod", defaultValue = "credit") String paymentMethod,
            @RequestParam(value = "bankProvider", required = false) String bankProvider,
            @RequestParam(value = "cardIssuer", required = false) String cardIssuer,
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
                    orderId =  orderService.createOrder(
                            user.getId(),
                            c.getProductId(),
                            c.getQuantity(),
                            c.getPrice(),
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
        String paymentLabel = formatPaymentMethodLabel(
                paymentMethod,
                "bank".equals(paymentMethod) ? bankProvider : null,
                "credit".equals(paymentMethod) ? cardIssuer : null
        );
        session.setAttribute("checkoutPaymentMethodLabel", paymentLabel);

        String name = fullName != null ? fullName.trim() : "";
        if (name.isEmpty() && user.getUsername() != null) {
            name = user.getUsername();
        }
        session.setAttribute(SESSION_CHECKOUT_NAME, name);

        String phoneVal = phone != null ? phone.trim() : "";
        if (phoneVal.isEmpty() && user.getPhone() != null) {
            phoneVal = user.getPhone();
        }
        session.setAttribute(SESSION_CHECKOUT_PHONE, phoneVal);

        String addr = address != null ? address.trim() : "";
        if (addr.isEmpty() && user.getAddress() != null) {
            addr = user.getAddress();
        }
        session.setAttribute(SESSION_CHECKOUT_ADDRESS, addr);

        for (String orderId : orderIds) {
            orderService.updateOrderShippingSnapshot(orderId, name, phoneVal, addr);
        }

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

                // 每次都创建新的 Pending 订单（不要复用旧的）
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

        double subtotal = orders.stream()
                .mapToDouble(i -> i.getOrder().getPrice() * i.getOrder().getQuantity())
                .sum();
        double deliveryFee = 2.00;
        double total = subtotal + deliveryFee;

        model.addAttribute("orders", orders);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("deliveryFee", deliveryFee);
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        model.addAttribute("deliveryRecipient", firstNonBlank((String) session.getAttribute(SESSION_CHECKOUT_NAME), user.getUsername()));
        model.addAttribute("deliveryPhone", firstNonBlank((String) session.getAttribute(SESSION_CHECKOUT_PHONE), user.getPhone()));
        model.addAttribute("deliveryAddress", firstNonBlank((String) session.getAttribute(SESSION_CHECKOUT_ADDRESS), user.getAddress()));
        model.addAttribute("isLoggedIn", true);
        Object label = session.getAttribute("checkoutPaymentMethodLabel");
        model.addAttribute("paymentMethodLabel", label != null ? label : "Not specified");

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
        String pmLabel = (String) session.getAttribute("checkoutPaymentMethodLabel");
        session.removeAttribute("checkoutPaymentMethodLabel");
        session.removeAttribute(SESSION_CHECKOUT_NAME);
        session.removeAttribute(SESSION_CHECKOUT_PHONE);
        session.removeAttribute(SESSION_CHECKOUT_ADDRESS);

        String successMsg = "Order completed!";
        if (pmLabel != null && !pmLabel.isBlank() && !"Not specified".equals(pmLabel)) {
            successMsg += " Payment method: " + pmLabel + ".";
        }
        redirectAttributes.addFlashAttribute("success", successMsg);
        return "redirect:/ganma/order";
    }

    /**
     * Human-readable label for the payment option chosen on checkout (card network or bank/e-wallet).
     */
    private static String firstNonBlank(String preferred, String fallback) {
        if (preferred != null && !preferred.isBlank()) {
            return preferred;
        }
        return fallback != null ? fallback : "";
    }

    private static String formatPaymentMethodLabel(String paymentMethod, String bankProvider, String cardIssuer) {
        if ("bank".equals(paymentMethod)) {
            return switch (bankProvider == null ? "" : bankProvider) {
                case "tng" -> "Touch 'n Go eWallet";
                case "maybank2u" -> "Maybank2u";
                case "cimb" -> "CIMB Clicks";
                case "publicbank" -> "Public Bank";
                default -> "Online banking / e-wallet";
            };
        }
        return switch (cardIssuer == null ? "" : cardIssuer) {
            case "visa" -> "Visa";
            case "maybank" -> "Maybank";
            case "ocbc" -> "OCBC Bank";
            case "mastercard" -> "Mastercard";
            case "amex" -> "American Express";
            default -> "Credit / debit card";
        };
    }
}
