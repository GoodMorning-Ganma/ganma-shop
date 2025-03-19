package com.ganmashop.service;

import com.ganmashop.dto.OrderDTO;
import com.ganmashop.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 30/12/2024
 */
@Service
public interface OrderService {
    void save(Order order);
    void updateOrderStatus(String orderId, String status);
    List<OrderDTO> getAllOrderDetails();
    List<OrderDTO> getOrderDetailsByUserId(String userId);
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(String userId);
}
