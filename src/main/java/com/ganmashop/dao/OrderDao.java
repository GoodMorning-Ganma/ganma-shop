package com.ganmashop.dao;

import com.ganmashop.dto.OrderDTO;
import com.ganmashop.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
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
                     @Param("price") Double price,
                     @Param("status") String status);

    void updateOrder(Order order);

    void updateOrderShipping(@Param("orderId") String orderId,
                            @Param("recipient") String recipient,
                            @Param("phone") String phone,
                            @Param("address") String address);

    void updateOrdersToPaid(List<String> orderIds);

    Order getOrderById(String orderId);
    OrderDTO getOrderDetailsById(String orderId);
    List<OrderDTO> getOrderDetailsByUserId(String userId);

    List<OrderDTO> getOrderDetailsByUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);
    List<OrderDTO> getAllOrderDetails();
    List<OrderDTO> getRecentOrderDetails();
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(String userId);
    List<Order> getPaidOrdersByUserId(String userId);
    List<Order> getDeliveredOrdersByUserId(String userId);
    List<Order> getPendingPaymentOrders(String userId);
    Order getPendingOrderByUserAndProduct(String userId, String productId);
    int countOrdersCreatedAfter(@Param("after") Date after);

    List<String> findOrderIdsCreatedAfter(@Param("after") Date after);

}
