package com.mdzidko.ordering.customersorders.customerorder.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public abstract class CustomerOrderEvent {
    protected final UUID orderId;
    protected final UUID customerId;
    protected final double value;
    protected final LocalDateTime timestamp;

    public abstract CustomerOrderEventType type();
}
