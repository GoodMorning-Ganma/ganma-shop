package com.ganmashop.dto;

import com.ganmashop.entity.Order;
import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;

/**
 * @author Jasonlzc
 * Date: 13/03/2025
 */
public class OrderDTO {
    private Order order;
    private Product product;
    private User user;

    // 必须有无参构造函数
    public OrderDTO() {}

    public OrderDTO(Order order, Product product, User user){
        this.order = order;
        this.product = product;
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
