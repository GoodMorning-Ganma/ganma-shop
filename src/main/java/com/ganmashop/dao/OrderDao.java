package com.ganmashop.dao;

import com.ganmashop.dto.OrderDTO;
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
    void updateOrder(Order order);
    Order getOrderById(String orderId);
    List<OrderDTO> getOrderDetailsByUserId(String userId);
    List<OrderDTO> getAllOrderDetails();
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(String userId);
}
