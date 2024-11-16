package com.ganmashop.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Desmondlzk
 * Date: 06/02/2024 - 8:17 PM
 */
@Mapper
public interface PermissionDao {
    List<String> getPermissionByUserId(String userId);
}
