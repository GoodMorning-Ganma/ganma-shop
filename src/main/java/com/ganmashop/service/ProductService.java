package com.ganmashop.service;

import com.ganmashop.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product findProductById(String id);
    List<Product> findAllProducts();
    Product findCategoryId(String categoryId);


}