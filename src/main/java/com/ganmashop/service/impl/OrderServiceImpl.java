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
    public List<Order> getOrdersByUserId(String userId) {
        return orderDao.getOrdersByUserId(userId);
    }

}