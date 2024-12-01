package com.ganmashop.dto;

import com.ganmashop.entity.Cart;
import com.ganmashop.entity.Product;


/**
 * @author Desmondlzk
 * Date: 01/12/2024
 */
public class CartProductDTO {
    private Cart cart;
    private Product product;

    public CartProductDTO(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

