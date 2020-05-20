package com.mdzidko.ordering.customersorders.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerDoesntExistsException extends RuntimeException {
    public CustomerDoesntExistsException(final UUID customerId) {
        super("Customer with id: " + customerId + " doesn't exists");
    }
}
