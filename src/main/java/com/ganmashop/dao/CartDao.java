package com.ganmashop.dao;

import com.ganmashop.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 24/11/2024
 */
@Mapper
public interface CartDao {
    // Find cart item by userId and productId
    Cart findCartByUserAndProduct(@Param("userId") String userId, @Param("productId") String productId);

    // Insert new cart item
    void save(Cart cart);

    // Update existing cart item
    void updateCart(Cart cart);

    // Find all cart items for a user
    List<Cart> findCartItemsByUser(@Param("userId") String userId);
}
