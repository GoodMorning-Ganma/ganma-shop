package com.ganmashop.dao;

import com.ganmashop.entity.Favourite;
import com.ganmashop.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 27/01/2025
 */
@Mapper
public interface FavouriteDao {
    void addToFavourite(Favourite favourite);
}
