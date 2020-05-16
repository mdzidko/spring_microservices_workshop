package com.mdzidko.ordering.exceptions;

import java.util.UUID;

public class NotEnoughCreditsException extends RuntimeException {
    public NotEnoughCreditsException(final UUID customerId) {
        super("Not enough credits for customer with id = " + customerId);
    }
}
