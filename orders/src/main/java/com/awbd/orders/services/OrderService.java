package com.awbd.orders.services;

import com.awbd.orders.models.Order;
import com.awbd.orders.models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderService {

    Order createOrder(List<Product> products, String username);

    Order getOrderById(Long orderId);

    List<Order> getOrdersByUser(String username);

    List<Order> getAllOrders();

    void deleteOrder(Long orderId);

    // Orders with webflux
    Mono<Order> getOrderByIdWebFlux(Long orderId);
    Flux<Order> getOrdersByUSerWebFlux(String username);
}
