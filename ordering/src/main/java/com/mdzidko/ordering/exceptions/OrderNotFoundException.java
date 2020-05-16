package com.mdzidko.ordering.exceptions;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(final UUID orderId) {
        super("Order with id = " + orderId + " not found");
    }
}
