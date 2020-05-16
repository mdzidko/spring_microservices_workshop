package com.mdzidko.ordering.exceptions;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(final UUID productId) {
        super("Product with id = " + productId + " not found");
    }
}
