package com.ganmashop.entity;

/**
 * @author Jasonlzc
 * Date: 27/01/2025
 */
public class Favourite {

    private String id;
    private String userId;
    private String productId;

    public Favourite(){

    }

    public Favourite(String id, String userId, String productId){
        this.id = id;
        this.userId = userId;
        this.productId = productId;
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
}
