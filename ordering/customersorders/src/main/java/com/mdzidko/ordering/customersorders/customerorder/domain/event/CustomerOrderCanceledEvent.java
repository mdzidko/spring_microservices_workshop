package com.mdzidko.ordering.customersorders.customerorder.domain.event;

import com.mdzidko.ordering.customersorders.customerorder.domain.dto.CustomerOrderLineDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Getter
public class CustomerOrderCanceledEvent extends CustomerOrderEvent {
    private final Collection<CustomerOrderLineDto> lines;

    public CustomerOrderCanceledEvent(final UUID orderId,
                                      final UUID customerId,
                                      final double value,
                                      final LocalDateTime timestamp,
                                      final Collection<CustomerOrderLineDto> lines) {
        super(orderId, customerId, value, timestamp);
        this.lines = lines;
    }

    @Override
    public CustomerOrderEventType type() {
        return CustomerOrderEventType.ORDER_CANCELED;
    }
}
