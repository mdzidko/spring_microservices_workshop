package com.mdzidko.ordering.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughCreditsException extends RuntimeException {
    public NotEnoughCreditsException(final UUID customerId) {
        super("Not enough credits for customer with id = " + customerId);
    }
}
