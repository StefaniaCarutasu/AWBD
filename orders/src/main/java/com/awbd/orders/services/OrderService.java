package com.awbd.orders.services;

import com.awbd.orders.models.Order;
import com.awbd.orders.models.Product;

import java.util.List;

public interface OrderService {

    Order createOrder(List<Product> products, String username);

    Order getOrderById(Long orderId);

    List<Order> getOrdersByUser(String username);

    List<Order> getAllOrders();
}
