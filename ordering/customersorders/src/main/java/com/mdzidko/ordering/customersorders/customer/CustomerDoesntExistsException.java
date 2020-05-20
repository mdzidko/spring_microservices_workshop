package com.mdzidko.ordering.customersorders.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerDoesntExistsException extends RuntimeException {
    public CustomerDoesntExistsException(final String message) {
        super(message);
    }
}
