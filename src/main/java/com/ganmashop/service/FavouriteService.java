package com.ganmashop.service;

import org.springframework.stereotype.Service;

/**
 * @author Jasonlzc
 * Date: 27/01/2025
 */
@Service
public interface FavouriteService {
    void addToFavourite(String userId, String productId);
}
