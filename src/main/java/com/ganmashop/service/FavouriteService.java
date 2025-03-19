package com.ganmashop.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 27/01/2025
 */
@Service
public interface FavouriteService {
    void addToFavourite(String userId, String productId);
    void deleteFavouriteItems(String userId, String productId);
}
