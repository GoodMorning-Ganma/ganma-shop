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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
