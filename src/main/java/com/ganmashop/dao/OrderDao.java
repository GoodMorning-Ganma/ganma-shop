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

    void insertOrder(@Param("id") String id,
                     @Param("userId") String userId,
                     @Param("productId") String productId,
                     @Param("quantity") int quantity,
                     @Param("price") double price,
                     @Param("status") String status);

    void updateOrder(Order order);
    void deletePendingPaymentOrders(String userId);
    Order getOrderById(String orderId);
    OrderDTO getOrderDetailsById(String orderId);
    List<OrderDTO> getOrderDetailsByUserId(String userId);
    List<OrderDTO> getAllOrderDetails();
    List<OrderDTO> getRecentOrderDetails();
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(String userId);
    List<Order> getPendingOrdersByUserId(String userId);
    List<Order> getDeliveredOrdersByUserId(String userId);
    List<Order> getPendingPaymentOrders(String userId);

}
