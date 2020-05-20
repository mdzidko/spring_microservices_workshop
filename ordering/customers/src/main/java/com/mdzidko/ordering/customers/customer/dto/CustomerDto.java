package com.mdzidko.ordering.customers.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private UUID id;
    private String name;
    private String surname;
    private double credits;
    private String street;
    private String city;
    private String postalCode;
    private int local;
}
