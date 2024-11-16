package com.ganmashop.service.impl;

import com.ganmashop.dao.UserDao;
import com.ganmashop.entity.User;
import com.ganmashop.service.UserService;
import com.ganmashop.utils.GenUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Desmondlzk
 * Date: 07/02/2024 - 5:33 PM
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserById(String id) {
        return userDao.findUserById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByUserEmail(String email) {
        return userDao.findByUserEmail(email);
    }

    @Override
    public User save(User user) {
        user.setId(GenUUID.getUUID());
        user.setUserType("customer");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
        return user;
    }

    @Override
    public void deleteUserById(String userId) {

    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }
}
