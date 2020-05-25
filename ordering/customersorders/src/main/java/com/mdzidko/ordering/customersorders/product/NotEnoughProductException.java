package com.mdzidko.ordering.customersorders.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughProductException extends RuntimeException {
    public NotEnoughProductException(final String message) {
        super(message);
    }
}
