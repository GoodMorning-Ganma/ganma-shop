package com.ganmashop.service;

import com.ganmashop.entity.User;

import java.util.List;

/**
 * @author Desmondlzk
 * Date: 07/02/2024 - 5:22 PM
 */
public interface UserService {

    User findUserById(String id);

    User findByUserEmail(String email);

    User findByUsername(String username);

    User save(User user);

    void deleteUserById(String userId);

    void updateUser(User user);
}