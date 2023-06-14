package com.awbd.orders.exceptions;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException() {
        super();
    }

    public OrderNotFoundException(String orderId) {
        super("Order with id = " + orderId + " not found");
    }
}
