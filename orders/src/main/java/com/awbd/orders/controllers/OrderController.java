package com.awbd.orders.controllers;

import com.awbd.orders.models.Order;
import com.awbd.orders.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/all/{username}")
    public ResponseEntity<Object> getAllOrdersByUser(@PathVariable String username) {
        List<Order> orderList = orderService.getOrdersByUser(username);

        if (orderList.isEmpty()) {
            return new ResponseEntity<>("No orders found for user: " + username, HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity<>(orderList, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/new/{username}")
    public ResponseEntity<Object> newOrder(@PathVariable String username, @RequestBody List<Long> prodIds) {
        Order newOrder = orderService.createOrder(prodIds, username);

        if (newOrder != null) {
            return new ResponseEntity<>("Order processed", HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>("Order could not be processed, try again later", HttpStatusCode.valueOf(500));
    }
}
