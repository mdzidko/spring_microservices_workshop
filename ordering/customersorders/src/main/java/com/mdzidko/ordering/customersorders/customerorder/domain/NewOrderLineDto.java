package com.mdzidko.ordering.customersorders.customerorder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewOrderLineDto {
    private UUID productId;
    private int quantity;
}
