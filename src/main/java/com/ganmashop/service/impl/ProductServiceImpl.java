package com.ganmashop.service.impl;

import com.ganmashop.dao.ProductDao;
import com.ganmashop.entity.Product;
import com.ganmashop.service.ProductService;
import com.ganmashop.utils.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDao productDao;

    @Override
    public Product findProductById(String productId) {
        if (Objects.isNull(productId)) {
            throw new BusinessException("ProductId cannot be null!");
        }
        try {
            return productDao.findProductById(productId);
        } catch (Exception e) {
            throw new BusinessException("Product not found!");
        }
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
