package com.ganmashop.entity;

import java.util.Date;

/**
 * @author Jasonlzc
 * Date: 29/12/2024
 */
public class Order {
    private String id;
    private String productId;
    private String userId;
    private int quantity;
    private double price;
    private String status;
    private Date createTime;
    private Date updateTime;

    public Order(String id, String productId, String userId, int quantity, double price, String status, Date createTime, Date updateTime){
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}


