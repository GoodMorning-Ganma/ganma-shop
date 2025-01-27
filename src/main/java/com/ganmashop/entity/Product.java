package com.ganmashop.entity;

import java.util.Date;

public class Product {
    private String id;
    private String userId;
    private String categoryId;
    private String name;
    private String description;
    private Double price;
    private String imageName;
    private Date createTime;
    private Date updateTime;

    public Product(){

    }

    public Product(String id, String userId, String categoryId, String name, String description, Double price, String imageName, Date createTime, Date updateTime){
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageName = imageName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId(){
        return categoryId;
    }
    public void setCategoryId(String categoryId){
        this.categoryId=categoryId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public Double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price=price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

