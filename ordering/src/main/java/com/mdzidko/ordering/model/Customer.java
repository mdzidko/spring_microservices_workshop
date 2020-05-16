package com.mdzidko.ordering.model;

import com.mdzidko.ordering.exceptions.NotEnoughCreditsException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Customer {
    private UUID id;
    private String name;
    private String surname;
    private Address address;
    private double credits;

    private Customer(final String name, final String surname, final Address address) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.credits = 0.0;
    }

    public static Customer create(String name, String surname, Address address){
        return new Customer(name, surname, address);
    }

    public Customer addCredits(final double credits) {
        this.credits += credits;
        return this;
    }

    public Customer reserveCredits(final double credits) {
        if(this.credits < credits){
            throw new NotEnoughCreditsException(this.id);
        }

        this.credits -= credits;
        return this;
    }
}
