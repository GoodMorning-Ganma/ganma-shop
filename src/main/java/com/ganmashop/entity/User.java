package com.ganmashop.entity;

import java.util.Date;

/**
 * @author Desmondlzk
 * Date: 06/02/2024 - 4:38 PM
 */
public class User {
    private String id;

    private String username;

    private String email;

    private String name;

    private String password;

    // 用户类型 0:管理员，1:普通顾客
    private String userType;

    private String phone;

    private String address;

    private Date createTime;

    private Date updateTime;

    public User() {
    }

    public User(String id, String username, String email, String name, String password, String userType, String phone, String address, Date createTime, Date updateTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.userType = userType;
        this.phone = phone;
        this.address = address;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
