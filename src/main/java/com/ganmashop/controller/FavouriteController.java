package com.ganmashop.controller;

import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import com.ganmashop.service.FavouriteService;
import com.ganmashop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 27/01/2025
 */
@Controller
@RequestMapping("/ganma")
public class FavouriteController {
    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private ProductService productService;

    @GetMapping("/favourite")
    public String showFavourites(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("isLoggedIn", user != null);
        if (user != null) {
            List<Product> favourites = productService.getFavouritesByUserId(user.getId());
            model.addAttribute("favourites", favourites);
            return "favourite";
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/favourite/add/{productId}")
    @ResponseBody
    public String addFavourite(@PathVariable("productId") String productId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            try {
                favouriteService.addToFavourite(loggedInUser.getId(), productId);
                return "Product added to favourites!";
            } catch (Exception e) {
                return "Invalid Add Favourite";
            }
        }
        return "Please Login to Add Favourite";
    }


}
