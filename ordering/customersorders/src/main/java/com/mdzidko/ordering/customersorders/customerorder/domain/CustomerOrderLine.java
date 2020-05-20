package com.mdzidko.ordering.customersorders.customerorder.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
class CustomerOrderLine {
    @Id
    private UUID id;
    private UUID productId;
    private int quantity;
    private double productPrice;

    private CustomerOrderLine(final UUID productId, final int quantity, final double productPrice) {
        this.id = UUID.randomUUID();
        this.productId = productId;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    static CustomerOrderLine create(final UUID productId, final int quantity, final double unitValue) {
        return new CustomerOrderLine(productId, quantity, unitValue);
    }

    CustomerOrderLine addQuantity(final int productQuantity) {
        this.quantity += productQuantity;
        return this;
    }

    double calculateTotalValue() {
        return this.quantity * this.productPrice;
    }

    CustomerOrderLineDto dto(){
        return CustomerOrderLineDto
                .builder()
                .productId(this.productId)
                .productQuantity(this.getQuantity())
                .value(this.calculateTotalValue())
                .build();
    }
}
