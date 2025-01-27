package com.ganmashop.service.impl;

import com.ganmashop.dao.FavouriteDao;
import com.ganmashop.dao.ProductDao;
import com.ganmashop.entity.Favourite;
import com.ganmashop.service.FavouriteService;
import com.ganmashop.utils.BusinessException;
import com.ganmashop.utils.GenUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
