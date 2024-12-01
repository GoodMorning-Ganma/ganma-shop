package com.ganmashop.dao;

import com.ganmashop.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProductDao {
   Product findProductById(String id);
    Product findCategoryId(String categoryId);
    List<Product> findAllProducts();
}
