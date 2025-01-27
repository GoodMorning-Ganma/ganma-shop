package com.ganmashop.dao;

import com.ganmashop.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 29/12/2024
 */
@Mapper
public interface OrderDao {
    void save(Order order);
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(String userId);
}
