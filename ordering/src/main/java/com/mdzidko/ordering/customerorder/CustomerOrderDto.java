package com.mdzidko.ordering.customerorder;

import com.mdzidko.ordering.customer.CustomerDto;
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
    private CustomerOrderStatus status;
    private double value;
    private CustomerDto customer;
}
