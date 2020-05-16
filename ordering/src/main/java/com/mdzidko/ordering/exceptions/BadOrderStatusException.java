package com.mdzidko.ordering.exceptions;

import com.mdzidko.ordering.model.CustomerOrderStatus;

public class BadOrderStatusException extends RuntimeException {
    public BadOrderStatusException(final CustomerOrderStatus orderStatus) {
        super("Bad order status: " + orderStatus);
    }
}
