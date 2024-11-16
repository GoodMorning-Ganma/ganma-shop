package com.ganmashop.entity;

import java.util.Date;

public class Category {
    private String id;
    private String typeName;
    private Date createTime;
    private Date updateTime;

    public Category(){

    }
    public Category(String id, String typeName, Date createTime, Date updateTime){
        this.id= id;
        this.typeName=typeName;
        this.createTime=createTime;
        this.updateTime=updateTime;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }

    public String getTypeName(){
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
