package com.mdzidko.ordering.customerorder;

public class BadOrderStatusException extends RuntimeException {
    public BadOrderStatusException(final CustomerOrderStatus orderStatus) {
        super("Bad order status: " + orderStatus);
    }
}
