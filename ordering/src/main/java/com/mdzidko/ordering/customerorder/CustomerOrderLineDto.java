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
public class CustomerOrderLineDto {
    private UUID productId;
    private String productName;
    private String productCode;
    private int productQuantity;
    private double value;
}
