package com.mdzidko.ordering.customer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@NoArgsConstructor
public class Customer {
    @Id
    private UUID id;
    private String name;
    private String surname;
    @Embedded
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

    public CustomerDto dto() {
        return CustomerDto
                .builder()
                .id(this.getId())
                .name(this.getName())
                .surname(this.getSurname())
                .credits(this.getCredits())
                .city(this.getAddress().getCity())
                .postalCode(this.getAddress().getPostalCode())
                .street(this.getAddress().getStreet())
                .local(this.getAddress().getLocal())
                .build();
    }
}
