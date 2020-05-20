package com.mdzidko.ordering.products.domain.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(final UUID productId) {
        super("Product with id = " + productId + " not found");
    }
}
