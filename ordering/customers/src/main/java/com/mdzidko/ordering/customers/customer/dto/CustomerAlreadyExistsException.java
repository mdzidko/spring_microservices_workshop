package com.mdzidko.ordering.customers.customer.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(final String name, final String surname) {
        super("Customer " + name + " " + " already exists");
    }
}
