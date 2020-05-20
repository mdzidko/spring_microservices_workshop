package com.mdzidko.ordering.customer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
class Address {
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

    static Address create(final String street, final String city, final String postalCode, final int local){
        return new Address(street, city, postalCode, local);
    }

}
