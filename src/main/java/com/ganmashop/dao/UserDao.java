package com.ganmashop.dao;

import com.ganmashop.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    User findUserById(@Param("id") String id);
    User findByUsername(@Param("username") String username);
    User findByUserEmail(@Param("email") String email);
    void save(User user);
    void updateUser(User user);
}
