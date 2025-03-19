package com.ganmashop.service;

import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProductService {
    Product findProductById(String id);
    List<Product> findAllProducts();
    Product findCategoryId(String categoryId);
    List<Product> getFavouritesByUserId(String userId);
    Product save(Product product);
    void updateProduct(Product product);
    void deleteProductById(String Id);
    List<Product> searchProducts(Map<String, Object> params);
    List<String> getAllCategories();


}