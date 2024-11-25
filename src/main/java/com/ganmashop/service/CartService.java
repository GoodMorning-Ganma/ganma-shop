package com.ganmashop.service;

import com.ganmashop.dao.CartDao;
import com.ganmashop.dao.ProductDao;
import com.ganmashop.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 24/11/2024
 */
@Service
public interface CartService {
    Cart findCartByUserAndProduct(String userId, String productId);
    void addToCart(Cart cart);
    List<Cart> getCartItems(String userId);



}
