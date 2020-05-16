package com.mdzidko.ordering.model;

import com.mdzidko.ordering.dtos.CustomerOrderLineDto;
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

    public CustomerOrderLineDto dto(){
        return CustomerOrderLineDto
                .builder()
                .productId(this.product.getId())
                .productCode(this.product.getCode())
                .productName(this.product.getName())
                .productQuantity(this.getQuantity())
                .value(this.calculateValue())
                .build();
    }
}
