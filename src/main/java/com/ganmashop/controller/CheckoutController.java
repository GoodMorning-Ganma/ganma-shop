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

import java.util.Arrays;
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
                .filter(c -> c.getProductId() != null)
                .filter(c -> productIds.contains(c.getProductId()))
                .map(c -> new CartProductDTO(c, productService.findProductById(c.getProductId())))
                .toList();


        double subtotal = selectedItems.stream()
                .mapToDouble(i -> i.getCart().getPrice())
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
    // CONFIRM ORDER (SUCCESS)
    // -----------------------------
    @PostMapping("/checkout/submit")
    public String submitOrder(
            @RequestParam List<String> productIds,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/auth/login";

        List<Cart> cartItems = cartService.getCartItems(user.getId());

        String lastOrderId = null;

        for (Cart c : cartItems) {
            if (productIds.contains(c.getProductId())) {

                // ✅ Check if a pending order already exists
                Order existingOrder = orderService.getPendingOrderByUserAndProduct(user.getId(), c.getProductId());
                if (existingOrder != null) {
                    lastOrderId = existingOrder.getId();// reuse existing pending order

                } else {
                    // CREATE new pending order
                    lastOrderId = orderService.createOrder(
                            user.getId(),
                            c.getProductId(),
                            c.getQuantity(),
                            c.getPrice() + 2,
                            "Pending"
                    );
                }
            }
        }

        // Remove selected items from cart
        cartService.deleteSelectedItems(user.getId(), productIds);

        if (lastOrderId != null) {
            return "redirect:/ganma/order/payment/" + lastOrderId;
        }

        redirectAttributes.addFlashAttribute("error", "Order error occurred.");
        return "redirect:/ganma/checkout";
    }




    @GetMapping("/order/payment/{orderId}")
    public String showPaymentPage(@PathVariable String orderId,
                                  Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must log in first.");
            return "redirect:/auth/login";
        }

        OrderDTO orderDTO = orderService.getOrderDetailsById(orderId);

        if (orderDTO == null) {
            redirectAttributes.addFlashAttribute("error", "Order not found.");
            return "redirect:/ganma/order";
        }

        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("user", orderDTO.getUser());
        model.addAttribute("product", orderDTO.getProduct());
        model.addAttribute("order", orderDTO.getOrder());

        return "payment";   // <---- new payment.html
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

        String lastOrderId = null;

        for (Cart c : cartItems) {
            if (productIds.contains(c.getProductId())) {

                lastOrderId = orderService.createOrder(
                        user.getId(),
                        c.getProductId(),
                        c.getQuantity(),
                        c.getPrice() + 2,
                        "Pending"   // <---- NEW STATUS
                );
            }
        }

        if (lastOrderId == null) {
            redirectAttributes.addFlashAttribute("error", "Unable to create pending order.");
            return "redirect:/ganma/checkout";
        }

        return "redirect:/ganma/orderPayment";
    }

    @PostMapping("/order/payment/success")
    public String paymentSuccess(
            @RequestParam String orderId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null){
            return "redirect:/auth/login";
        }

        // Step 2: update the status of the paid order
        orderService.updateOrderStatus(orderId, "Paid");

        // Step 3: redirect to order page
        redirectAttributes.addFlashAttribute("success", "Payment successful!");
        return "redirect:/ganma/order";


    }


}
