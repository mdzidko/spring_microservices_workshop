package com.mdzidko.ordering.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Address {
    private String street;
    private String city;
    private String postalCode;
    private int local;

    private Address(final String street, final String city, final String postalCode, final int local) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.local = local;
    }

    public static Address create(final String street, final String city, final String postalCode, final int local){
        return new Address(street, city, postalCode, local);
    }

}
