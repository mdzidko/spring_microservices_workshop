package com.mdzidko.ordering.customersorders.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughCreditsException extends RuntimeException {
    public NotEnoughCreditsException(final String message) {
        super(message);
    }
}
