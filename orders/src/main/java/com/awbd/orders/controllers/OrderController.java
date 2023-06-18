package com.awbd.orders.controllers;

import com.awbd.orders.exceptions.OrderNotFoundException;
import com.awbd.orders.models.Order;
import com.awbd.orders.models.Product;
import com.awbd.orders.services.OrderService;
import com.awbd.orders.services.ProductClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductClient productClient;

    @Operation(summary = "Get all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders returned",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "500", description = "Orders could not be retrieved",
                    content = @Content)})
    @GetMapping(value = "/list", produces = {"application/hal+json"})
    public ResponseEntity<Object> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        try {
            List<Order> ordersWithLinks = getObjectsWithLinks(orders);
            return new ResponseEntity<>(getObjectsWithLinks(orders), HttpStatusCode.valueOf(200));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(500));
        }


    }

    @Operation(summary = "Get all orders for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders returned",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "500", description = "Orders could not be retrieved",
                    content = @Content)})
    @GetMapping("/list/{username}")
    public ResponseEntity<Object> getAllOrdersByUser(@PathVariable String username) {
        List<Order> orderList = orderService.getOrdersByUser(username);

        if (orderList.isEmpty()) {
            logger.debug("No orders found for user: " + username);
            return new ResponseEntity<>("No orders found for user: " + username, HttpStatusCode.valueOf(400));
        } else {
            logger.debug("Orders for user: " + username + "have been found");
            return new ResponseEntity<>(getObjectsWithLinks(orderList), HttpStatusCode.valueOf(200));
        }

    }

    @NotNull
    private List<Order> getObjectsWithLinks(List<Order> orderList) {
        for (final Order order : orderList) {
            Link selfLink = linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel();
            order.add(selfLink);
            Link deleteLink = linkTo(methodOn(OrderController.class).deleteOrder(order.getId())).withRel("deleteOrder");
            order.add(deleteLink);
        }
        return orderList;
    }

    @Operation(summary = "Add new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "500", description = "Order could not be created",
                    content = @Content)})
    @PostMapping("/new/{username}")
    public ResponseEntity<Object> newOrder(@PathVariable String username, @RequestBody List<Long> prodIds) {
        try {
            List<Product> products = prodIds.stream().map(id -> productClient.findById(id)).collect(Collectors.toList());
            Order newOrder = orderService.createOrder(products, username);

            if (newOrder != null) {
                logger.debug("Order processed for user: " + username );
                return new ResponseEntity<>("Order processed", HttpStatusCode.valueOf(200));
            }
            return new ResponseEntity<>("Order could not be processed, try again later", HttpStatusCode.valueOf(500));
        } catch (Exception e) {
            logger.debug("Order could not be processed for user: " + username );
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(500));
        }
    }


    @Operation(summary = "Delete order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Order could not be found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Order could not be deleted",
                    content = @Content)})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return new ResponseEntity<>("Order deleted", HttpStatusCode.valueOf(200));
        } catch (OrderNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(404));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name="orderById", fallbackMethod = "getOrderFallback")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
        try {
            logger.debug("Trying to retrieve order with id " + id);
            Order order = orderService.getOrderById(id);
            logger.info("Order with id " + id + " was found");
            return new ResponseEntity<>(order, HttpStatusCode.valueOf(200));
        } catch (OrderNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(404));
        }
    }

    private ResponseEntity<Object> getOrderFallback(Throwable throwable) {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatusCode.valueOf(200));
    }
}
