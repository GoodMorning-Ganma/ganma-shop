package com.ganmashop.dao;

import com.ganmashop.entity.Favourite;
import com.ganmashop.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 27/01/2025
 */
@Mapper
public interface FavouriteDao {
    void addToFavourite(Favourite favourite);
    void deleteFavouriteItems(String userId, String productId);
    int checkFavouriteExists(@Param("userId") String userId, @Param("productId") String productId);
}
