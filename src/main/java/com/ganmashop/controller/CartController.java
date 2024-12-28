package com.ganmashop.controller;

import com.ganmashop.dto.CartProductDTO;
import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import com.ganmashop.entity.Cart;
import com.ganmashop.service.CartService;
import com.ganmashop.service.ProductService;
import com.ganmashop.utils.BusinessException;
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
 * Date: 16/11/2024
 */
@Controller
@RequestMapping("/ganma")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @GetMapping("/cart")
    public String showCartPage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (Objects.isNull(user)) {
            redirectAttributes.addFlashAttribute("error", "You are required to login.");
            return "redirect:/auth/login";
        } else {
            model.addAttribute("isLoggedIn", true);
            try {
                // 首先查看当前用户有没有曾经把product添加进cart里
                List<Cart> cartItems = cartService.getCartItems(user.getId());
                // 然后通过cartItems的productId，link product table 获取到对应的product信息包括名字
                List<CartProductDTO> cartProductDetails = cartItems.stream()
                        .map(cart -> {
                            Product product = productService.findProductById(cart.getProductId());
                            return new CartProductDTO(cart, product);
                        })
                        .toList();
                model.addAttribute("cartProductDetails", cartProductDetails);
                return "cart";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "index";
            }
        }
    }

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam("productId") String productId,
            @RequestParam("quantity") int quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (Objects.isNull(user)) {
            redirectAttributes.addFlashAttribute("error", "You are required to login.");
            return "redirect:/auth/login";
        }

        try {
            // Retrieve the product details
            Product product = productService.findProductById(productId);

            // Create a new cart item
            Cart cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(user.getId());
            cart.setQuantity(quantity);
            cart.setPrice(product.getPrice() * quantity); // Calculate total price for this quantity

            // Save or update cart item
            cartService.save(cart);

            redirectAttributes.addFlashAttribute("success", "Product added to cart successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add product to cart: " + e.getMessage());
        }

        return "redirect:/ganma/cart"; // Redirect to the cart page
    }

    @PostMapping("/cart/deleteSelected")
    public String deleteSelectedItems(@RequestParam("productIds") List<String> productIds, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (Objects.isNull(user)) {
            redirectAttributes.addFlashAttribute("error", "You are required to login.");
            return "redirect:/auth/login";
        }

        try {
            cartService.deleteSelectedItems(user.getId(), productIds);
            redirectAttributes.addFlashAttribute("success", "Selected items removed from the cart.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete selected items.");
        }

        return "redirect:/ganma/cart";
    }


}
