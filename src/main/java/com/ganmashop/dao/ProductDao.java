package com.ganmashop.dao;

import com.ganmashop.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProductDao {
   Product findProductId(String id);
    Product findCategoryId(String categoryId);
    Product findProductName(String name);
    Product findProductPrice(Double price);
    Product findProductImg(String imageName);
    Product findProductDesc(String description);
    List<Product> findAllProducts();
}
