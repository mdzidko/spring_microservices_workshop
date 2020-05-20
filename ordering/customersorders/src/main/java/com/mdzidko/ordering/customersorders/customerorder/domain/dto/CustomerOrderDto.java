package com.mdzidko.ordering.customersorders.customerorder.domain.dto;

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
    private String status;
    private double value;
}
