package com.mdzidko.ordering.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductExistsException extends RuntimeException {
    public ProductExistsException(final String code) {
        super("Product with code " + code + " + already exists");
    }
}
