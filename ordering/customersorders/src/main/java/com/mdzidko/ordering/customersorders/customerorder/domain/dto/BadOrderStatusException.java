package com.mdzidko.ordering.customersorders.customerorder.domain.dto;

public class BadOrderStatusException extends RuntimeException {
    public BadOrderStatusException(final String orderStatus) {
        super("Bad order status: " + orderStatus);
    }
}
