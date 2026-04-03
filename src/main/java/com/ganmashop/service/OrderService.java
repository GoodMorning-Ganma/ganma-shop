package com.ganmashop.service;

import com.ganmashop.dto.OrderDTO;
import com.ganmashop.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Jasonlzc
 * Date: 30/12/2024
 */
@Service
public interface OrderService {
    void save(Order order);
    String createOrder(String userId, String productId, int quantity, Double price, String status);

    void updateOrderStatus(String orderId, String status);
    void updateOrdersToPaid(List<String> orderIds);

    List<OrderDTO> getAllOrderDetails();
    List<OrderDTO> getRecentOrderDetails();
    List<OrderDTO> getOrderDetailsByUserId(String userId);
    OrderDTO getOrderDetailsById(String orderId);
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(String userId);
    List<Order> getPaidOrdersByUserId(String userId);
    List<Order> getDeliveredOrdersByUserId(String userId);
    List<Order> getPendingPaymentOrders(String userId);
    Order getPendingOrderByUserAndProduct(String userId, String productId);

    int countOrdersCreatedAfter(Date after);

}
