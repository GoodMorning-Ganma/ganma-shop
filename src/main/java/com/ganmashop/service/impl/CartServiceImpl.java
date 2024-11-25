package com.ganmashop.service.impl;

import com.ganmashop.dao.CartDao;
import com.ganmashop.entity.Cart;
import com.ganmashop.service.CartService;
import com.ganmashop.utils.BusinessException;
import com.ganmashop.utils.GenUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartDao cartDao;

    // Find cart item by user and product
    @Override
    public Cart findCartByUserAndProduct(String userId, String productId) {
        return cartDao.findCartByUserAndProduct(userId, productId);
    }

    // Add product to cart
    @Override
    public void addToCart(Cart cart) {
        if (Objects.isNull(cart.getUserId()) || Objects.isNull(cart.getProductId())) {
            throw new BusinessException("User or Product is Invalid!");
        }
        try {
            Cart cartItem = cartDao.findCartByUserAndProduct(cart.getUserId(), cart.getProductId());
            if (Objects.nonNull(cartItem)) {
                cartItem.setId(GenUUID.getUUID());
                cartItem.setQuantity(cart.getQuantity());
                cartItem.setPrice(cart.getPrice());

                cartDao.insertCart(cartItem);
            }
        } catch (Exception e) {
//            logger.error("获取商店异常", e);
            throw new BusinessException("获取失败，请稍后再试");
        }
    }

    // Get cart items for a user
    @Override
    public List<Cart> getCartItems(String userId) {
        return cartDao.findCartItemsByUser(userId);
    }
}
