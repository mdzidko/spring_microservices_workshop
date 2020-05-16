package com.mdzidko.ordering.model;

import com.mdzidko.ordering.exceptions.NotEnoughProductException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Product {
    private UUID id;
    private String code;
    private String name;
    private double price;
    private int quantity;

    private Product(final String code, final String name, final double price) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = 0;
    }

    static public Product create(final String code, final String name, final double price){
       return new Product(code, name, price);
    }

    public Product changePrice(final double price){
        this.price = price;
        return this;
    }

    public Product addToStock(final int quantity) {
        this.quantity += quantity;
        return this;
    }

    public Product removeFromStock(final int quantity) {
        if(this.quantity < quantity){
            throw new NotEnoughProductException(this.id);
        }

        this.quantity -= quantity;
        return this;
    }
}
