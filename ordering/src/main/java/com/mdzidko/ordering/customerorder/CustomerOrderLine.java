package com.mdzidko.ordering.customerorder;

import com.mdzidko.ordering.product.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
public class CustomerOrderLine {
    @Id
    private UUID id;
    @OneToOne
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
