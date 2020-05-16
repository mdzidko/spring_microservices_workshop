package com.mdzidko.ordering.exceptions;


import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(final UUID customerId) {
        super("Customer with id = " + customerId +" not found");
    }
}
