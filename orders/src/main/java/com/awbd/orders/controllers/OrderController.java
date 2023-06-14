package com.awbd.orders.controllers;

import com.awbd.orders.models.Order;
import com.awbd.orders.models.Product;
import com.awbd.orders.services.OrderService;
import com.awbd.orders.services.ProductClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductClient productClient;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/all/{username}")
    public ResponseEntity<Object> getAllOrdersByUser(@PathVariable String username) {
        List<Order> orderList = orderService.getOrdersByUser(username);

        if (orderList.isEmpty()) {
            logger.debug("No orders found for user: " + username);
            return new ResponseEntity<>("No orders found for user: " + username, HttpStatusCode.valueOf(400));
        }
        logger.debug("Orders for user: " + username + "have been found");
        return new ResponseEntity<>(orderList, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/new/{username}")
    public ResponseEntity<Object> newOrder(@PathVariable String username, @RequestBody List<Long> prodIds) {

        List<Product> products = productClient.findAllByIds(prodIds);
        Order newOrder = orderService.createOrder(products, username);

        if (newOrder != null) {
            logger.debug("Order processed for user: " + username );
            return new ResponseEntity<>("Order processed", HttpStatusCode.valueOf(200));
        }
        logger.debug("Order could not be processed for user: " + username );
        return new ResponseEntity<>("Order could not be processed, try again later", HttpStatusCode.valueOf(500));
    }
}
