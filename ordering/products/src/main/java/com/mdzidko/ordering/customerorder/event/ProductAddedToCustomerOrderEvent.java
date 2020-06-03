package com.mdzidko.ordering.customerorder.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ProductAddedToCustomerOrderEvent extends CustomerOrderEvent{
    private final UUID productId;
    private final double productPrice;
    private final int productQuantity;

    public ProductAddedToCustomerOrderEvent(final UUID orderId,
                                            final UUID customerId,
                                            final double value,
                                            final LocalDateTime timestamp,
                                            final UUID productId,
                                            final double productPrice,
                                            final int productQuantity) {
        super(orderId, customerId, value, timestamp);
        this.productId = productId;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    @Override
    public CustomerOrderEventType type() {
        return CustomerOrderEventType.PRODUCT_ADDED_TO_ORDER;
    }
}
