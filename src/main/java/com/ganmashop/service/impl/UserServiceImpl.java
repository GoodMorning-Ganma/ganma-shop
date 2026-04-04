package com.ganmashop.service.impl;

import com.ganmashop.dao.UserDao;
import com.ganmashop.entity.User;
import com.ganmashop.service.UserService;
import com.ganmashop.utils.GenUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public User save(User user) {
        user.setId(GenUUID.getUUID());
        user.setUserType("customer");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
        return user;
    }



    @Override
    public User adminUser(User admin) {
        admin.setId(GenUUID.getUUID());
        admin.setUserType("admin");
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        userDao.save(admin);
        return admin;
    }

    @Override
    public List<User> findAllAdmins() {
        return userDao.findAllAdmins();
    }

    @Override
    public void deleteUserById(String userId) {
        userDao.deleteUserById(userId);
    }

    @Override
    public void updateUser(User incoming) {
        User existing = userDao.findUserById(incoming.getId());
        if (existing == null) {
            return;
        }
        if (incoming.getEmail() != null) {
            existing.setEmail(incoming.getEmail());
        }
        if (incoming.getName() != null) {
            existing.setName(incoming.getName());
        }
        if (incoming.getPhone() != null) {
            existing.setPhone(incoming.getPhone());
        }
        if (incoming.getAddress() != null) {
            existing.setAddress(incoming.getAddress());
        }
        if (incoming.getUsername() != null && !incoming.getUsername().isBlank()) {
            existing.setUsername(incoming.getUsername().trim());
        }
        if (incoming.getPassword() != null && !incoming.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(incoming.getPassword()));
        }
        userDao.updateUser(existing);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }
}
