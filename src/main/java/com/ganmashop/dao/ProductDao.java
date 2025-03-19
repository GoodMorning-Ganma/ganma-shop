package com.ganmashop.dao;

import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductDao {
   Product findProductById(String id);
    Product findCategoryId(String categoryId);
    List<Product> findAllProducts();
    List<Product> getFavouritesByUserId(String userId);
    void save(Product product);
    void updateProduct(Product product);
    void deleteProductById(String Id);
    List<Product> searchProducts(Map<String, Object> params);
    List<String> selectAllCategories();
}
