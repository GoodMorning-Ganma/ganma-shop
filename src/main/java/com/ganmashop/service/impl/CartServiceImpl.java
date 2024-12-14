package com.ganmashop.service.impl;

        import com.ganmashop.dao.CartDao;
        import com.ganmashop.entity.Cart;
        import com.ganmashop.service.CartService;
        import com.ganmashop.utils.BusinessException;
        import com.ganmashop.utils.GenUUID;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import java.util.List;
        import java.util.Objects;

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
            // 检查购物车里有没有存在当前用户已经选择的product
            Cart existingCartItems = cartDao.findCartByUserAndProduct(cart.getUserId(), cart.getProductId());
            if (Objects.nonNull(existingCartItems)) {
                // 如果已存在就只增加product数量
                existingCartItems.setQuantity(existingCartItems.getQuantity() + cart.getQuantity());
                existingCartItems.setPrice(existingCartItems.getPrice() + cart.getPrice() * cart.getQuantity());
                cartDao.updateCart(existingCartItems);
            } else {
                cart.setId(GenUUID.getUUID());
                cartDao.save(cart);
            }
        } catch (Exception e) {
            throw new BusinessException("Internal server error. Please try again.");
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

    @Override
    public void deleteSelectedItems(String userId, List<String> productIds) {
        if (Objects.isNull(userId) || productIds.isEmpty()) {
            throw new BusinessException("UserId or ProductIds cannot be null!");
        }
        try {
            cartDao.deleteSelectedItems(userId, productIds);
        } catch (Exception e) {
            throw new BusinessException("Failed to delete selected cart items.");
        }
    }


}
