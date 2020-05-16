package com.mdzidko.ordering.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class CustomerOrderLine {
    private UUID id;
    private Product product;
    private int quantity;

    private CustomerOrderLine(final Product product, final int quantity) {
        this.id = UUID.randomUUID();
        this.product = product;
        this.quantity = quantity;
    }

    public static CustomerOrderLine create(final Product product, final int quantity) {
        return new CustomerOrderLine(product, quantity);
    }

    public double calculateValue() {
        return product.getPrice() * quantity;
    }

    public CustomerOrderLine addQuantity(final int productQuantity) {
        this.quantity += productQuantity;
        return this;
    }
}
