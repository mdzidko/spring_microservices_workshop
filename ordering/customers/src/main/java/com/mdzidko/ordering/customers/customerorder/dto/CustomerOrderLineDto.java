package com.mdzidko.ordering.customers.customerorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOrderLineDto {
    private UUID productId;
    private int productQuantity;
    private double value;
}
