package com.awbd.orders.services.impl;

import com.awbd.orders.daos.ProductsDao;
import com.awbd.orders.exceptions.OrderNotFoundException;
import com.awbd.orders.daos.OrdersDao;
import com.awbd.orders.models.Order;
import com.awbd.orders.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.awbd.orders.services.OrderService;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private ProductsDao productsDao;

    @Override
    public Order createOrder(List<Product> products, String username) {
        Order order = new Order(products, username);
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

    @Override
    public void deleteOrder(Long orderId) {
        Optional<Order> order = ordersDao.findById(orderId);
        if (order.isPresent()) {
            ordersDao.deleteById(orderId);
        } else {
            throw new OrderNotFoundException(orderId.toString());
        }
    }

    @Override
    public Mono<Order> getOrderByIdWebFlux(Long orderId) {
        Order order = this.getOrderById(orderId);
        return Mono.just(order);
    }

    @Override
    public Flux<Order> getOrdersByUSerWebFlux(String username) {
        List<Order> orders = this.getOrdersByUser(username);
        return Flux.fromIterable(orders);
    }


}
