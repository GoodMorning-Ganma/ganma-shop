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

    @Override
    public Cart findCartByUserAndProduct(String userId, String productId) {
        if (Objects.isNull(userId) || Objects.isNull(productId)) {
            throw new BusinessException("UserId or ProductId cannot be null!");
        }
        try {
            return cartDao.findCartByUserAndProduct(userId, productId);
        } catch (Exception e) {
            throw new BusinessException("UserId or ProductId invalid. Cart item not found");
        }
    }

    public void updateCart(Cart cart){
        cartDao.updateCart(cart);
    }
    
    @Override
    public void save(Cart cart) {
        if (Objects.isNull(cart.getUserId()) || Objects.isNull(cart.getProductId())) {
            throw new BusinessException("UserId or ProductId cannot be null!");
        }

        try {
            //TODO: 在把用户选好的product添加到cart table之前，
            // 应该查询对应的物品是否已经存在，如果已存在那就只增加quantity
            if(Objects.nonNull(cart.getProductId())){
                cart.setQuantity(cart.getQuantity() + cart.getQuantity());
                cartDao.updateCart(cart);
            }else{
                cart.setUserId(GenUUID.getUUID());
                cartDao.save(cart);
            }

        } catch (Exception e) {
            throw new BusinessException("Internal server error. Please try again");
        }
    }

    @Override
    public List<Cart> getCartItems(String userId) {
        if (Objects.isNull(userId)) {
            throw new BusinessException("UserId cannot be null!");
        }
        try {
            return cartDao.findCartItemsByUser(userId);
        } catch (Exception e) {
            throw new BusinessException("UserId invalid. Cart item not found");
        }
    }
}
