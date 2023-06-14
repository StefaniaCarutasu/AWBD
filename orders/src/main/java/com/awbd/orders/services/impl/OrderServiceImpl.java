package com.awbd.orders.services.impl;

import com.awbd.orders.daos.ProductsDao;
import com.awbd.orders.exceptions.OrderNotFoundException;
import com.awbd.orders.daos.OrdersDao;
import com.awbd.orders.models.Order;
import com.awbd.orders.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.awbd.orders.services.OrderService;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private ProductsDao productsDao;
    

    @Override
    public Order createOrder(List<Long> products, String username) {
        List<Product> prodList = productsDao.findAllById(products);
        Order order = new Order(prodList, username);
        return ordersDao.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        Optional<Order> order = ordersDao.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new OrderNotFoundException(orderId.toString());
        }
    }

    @Override
    public List<Order> getOrdersByUser(String username) {
        return ordersDao.getAllByUsername(username);
    }

    @Override
    public List<Order> getAllOrders() {
        return ordersDao.findAll();
    }
}