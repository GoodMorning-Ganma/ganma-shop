package com.ganmashop.service.impl;

import com.ganmashop.dao.CartDao;
import com.ganmashop.dao.OrderDao;
import com.ganmashop.dto.OrderDTO;
import com.ganmashop.entity.Order;
import com.ganmashop.service.OrderService;
import com.ganmashop.utils.BusinessException;
import com.ganmashop.utils.GenUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Jasonlzc
 * Date: 30/12/2024
 */

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CartDao cartDao;

    @Override
    public void save(Order order) {
        if (Objects.isNull(order.getUserId()) || Objects.isNull(order.getProductId())) {
            throw new BusinessException("UserId and ProductID cannot be null!");
        }
        try {
            order.setId(GenUUID.getUUID());
            orderDao.save(order);
        } catch (Exception e) {
            throw new BusinessException("UserId and ProductId invalid. Order item not found");
        }
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(status);
        orderDao.updateOrder(order);
    }

    public List<OrderDTO> getAllOrderDetails() {
        return orderDao.getAllOrderDetails();
    }

    @Override
    public List<OrderDTO> getOrderDetailsByUserId(String userId) {
        return orderDao.getOrderDetailsByUserId(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    @Override
    public List<OrderDTO> getRecentOrderDetails() {
        return orderDao.getRecentOrderDetails();
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        return orderDao.getOrdersByUserId(userId);
    }

    public List<Order> getPendingOrdersByUserId(String userId) {
        return orderDao.getPendingOrdersByUserId(userId);
    }

    @Override
    public List<Order> getDeliveredOrdersByUserId(String userId) {
        return orderDao.getDeliveredOrdersByUserId(userId);
    }

    @Override
    public OrderDTO getOrderDetailsById(String orderId) {
        return orderDao.getOrderDetailsById(orderId);
    }

    @Override
    public String createOrder(String userId, String productId, int quantity, double price, String status) {

        String id = GenUUID.getUUID();  // auto-generate order ID

        orderDao.insertOrder(id, userId, productId, quantity, price, status);
        return id;
    }
    @Override
    public List<Order> getPendingPaymentOrders(String userId) {
        return orderDao.getPendingPaymentOrders(userId);
    }

    @Override
    public Order getPendingOrderByUserAndProduct(String userId, String productId) {
        List<Order> pendingOrders = orderDao.getPendingPaymentOrders(userId);
        if (pendingOrders != null) {
            for (Order o : pendingOrders) {
                if (o.getProductId().equals(productId)) {
                    return o;  // return existing pending order
                }
            }
        }
        return null; // no pending order exists
    }


}
