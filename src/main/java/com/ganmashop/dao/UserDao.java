package com.ganmashop.dao;

import com.ganmashop.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Desmondlzk
 * Date: 06/02/2024 - 8:17 PM
 */
@Mapper
public interface UserDao {
    User findUserById(String id);
    User findByUserEmail(String email);
    User findByUsername(String username);
    void save(User user);
    void deleteUserById(String userId);
    void updateUser(User user);
}
