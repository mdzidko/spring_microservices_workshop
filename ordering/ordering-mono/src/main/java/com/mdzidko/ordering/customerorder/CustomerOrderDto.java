package com.mdzidko.ordering.customerorder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOrderDto {
    private UUID id;
    private UUID customerId;
    private CustomerOrderStatus status;
    private double value;
}
