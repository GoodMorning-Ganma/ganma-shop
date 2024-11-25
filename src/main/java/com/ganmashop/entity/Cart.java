package com.ganmashop.entity;

import java.util.Date;

public class Cart {
    private String id;
    private String productId;
    private String userId;
    private int quantity;
    private double price;
    private Date createTime;
    private Date updateTime;

    public Cart(){

    }
    public Cart(String id, String productId, String userId, int quantity, double price, Date createTime, Date updateTime){
        this.id=id;
        this.productId=productId;
        this.userId=userId;
        this.quantity=quantity;
        this.price=price;
        this.createTime=createTime;
        this.updateTime=updateTime;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }

    public String getProductId(){
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price=price;
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
