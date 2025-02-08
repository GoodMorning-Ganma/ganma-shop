package com.ganmashop.controller;

import com.ganmashop.entity.Cart;
import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import com.ganmashop.service.CartService;
import com.ganmashop.service.FavouriteService;
import com.ganmashop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Jasonlzc
 * Date: 18/11/2024
 */
@Controller
@RequestMapping("/ganma")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @Autowired
    private FavouriteService favouriteService;

    @GetMapping("/product/{id}")
    public String showProductDetail(HttpSession session, @PathVariable("id") String id, Model model) {
        Product product = productService.findProductById(id);
        if (Objects.isNull(product)) {
            model.addAttribute("error", "Product not found!");
            return "error";
        }
        model.addAttribute("product", product);
        return "product";
    }


}
