package com.mdzidko.ordering.exceptions;

public class ProductExistsException extends RuntimeException {
    public ProductExistsException(final String code) {
        super("Product with code " + code + " + already exists");
    }
}
