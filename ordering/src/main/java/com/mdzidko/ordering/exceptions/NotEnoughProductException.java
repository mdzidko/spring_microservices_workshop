package com.mdzidko.ordering.exceptions;

import java.util.UUID;

public class NotEnoughProductException extends RuntimeException {
    public NotEnoughProductException(final UUID productId) {
        super("Not enough product with id = " + productId);
    }
}
