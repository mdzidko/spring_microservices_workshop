package com.mdzidko.ordering.products.domain.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughProductException extends RuntimeException {
    public NotEnoughProductException(final UUID productId) {
        super("Not enough product with id = " + productId);
    }
}
