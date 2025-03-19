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
    Cart findCartByProductIdAndUserId(@Param("userId") String userId, @Param("productId") String productId);
    void save(Cart cart);
    void updateCart(Cart cart);
    void deleteSelectedItems(@Param("userId") String userId, @Param("productIds") List<String> productIds);
    List<Cart> findCartItemsByUser(@Param("userId") String userId);

}
