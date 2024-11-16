package com.ganmashop.service.impl;

import com.ganmashop.dao.ProductDao;
import com.ganmashop.entity.Product;
import com.ganmashop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDao productDao;

    public Product findProductName(String name){
        return productDao.findProductName(name);
    }

    @Override
    public Product findProductId(String id) {
        return productDao.findProductId(id);
    }

    @Override
    public Product findCategoryId(String categoryId){
        return productDao.findCategoryId(categoryId);
    }
    @Override
    public Product findProductPrice(Double price){
        return productDao.findProductPrice(price);
    }
    @Override
    public Product findProductImg(String imageName){
        return productDao.findProductImg(imageName);
    }
    @Override
    public Product findProductDesc(String description){
        return productDao.findProductDesc(description);
    }
    @Override
    public List<Product> findAllProducts() {
        return productDao.findAllProducts();
    }

}
