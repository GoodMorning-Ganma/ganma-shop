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

    @Override
    public Product findProductId(String id) {
        return productDao.findProductId(id);
    }

    @Override
    public Product findCategoryId(String categoryId){
        return productDao.findCategoryId(categoryId);
    }

    @Override
    public List<Product> findAllProducts() {
        return productDao.findAllProducts();
    }

}
