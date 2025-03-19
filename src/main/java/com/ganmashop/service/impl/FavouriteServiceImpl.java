package com.ganmashop.service.impl;

import com.ganmashop.dao.FavouriteDao;
import com.ganmashop.dao.ProductDao;
import com.ganmashop.entity.Favourite;
import com.ganmashop.service.FavouriteService;
import com.ganmashop.utils.BusinessException;
import com.ganmashop.utils.GenUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Jasonlzc
 * Date: 27/01/2025
 */

@Service
public class FavouriteServiceImpl implements FavouriteService {

    @Autowired
    ProductDao productDao;
    @Autowired
    FavouriteDao favouriteDao;

    @Override
    public void addToFavourite(String userId, String productId) {
        if (Objects.isNull(productId) || Objects.isNull(userId)) {
            throw new BusinessException("Invalid user or product ID");
        }
        int exists = favouriteDao.checkFavouriteExists(userId, productId);
        if (exists > 0) {
            throw new BusinessException("Product already in favourites");
        }
        try{
            Favourite favourite = new Favourite();
            favourite.setId(GenUUID.getUUID());
            favourite.setProductId(productId);
            favourite.setUserId(userId);
            favouriteDao.addToFavourite(favourite);
        }catch(Exception e){
            throw new BusinessException("Internal server error. Please try again.");
        }
    }

    @Override
    public void deleteFavouriteItems(String userId, String productId) {
        if (Objects.isNull(userId) || productId.isEmpty()) {
            throw new BusinessException("UserId or ProductIds cannot be null!");
        }
        try {
            favouriteDao.deleteFavouriteItems(userId, productId);
        } catch (Exception e) {
            throw new BusinessException("Failed to delete selected cart items.");
        }
    }
}
