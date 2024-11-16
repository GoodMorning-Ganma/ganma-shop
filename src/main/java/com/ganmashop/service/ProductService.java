package com.ganmashop.service;

import com.ganmashop.entity.Product;
import com.ganmashop.dao.ProductDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product findProductId(String id);
    List<Product> findAllProducts();
    Product findCategoryId(String categoryId);
    Product findProductName(String name);
    Product findProductPrice(Double price);
    Product findProductImg(String imageName);
    Product findProductDesc(String description);


}