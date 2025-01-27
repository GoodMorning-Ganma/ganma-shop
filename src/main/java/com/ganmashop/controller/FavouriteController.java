package com.ganmashop.controller;

import com.ganmashop.entity.User;
import com.ganmashop.service.FavouriteService;
import com.ganmashop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
