package com.mdzidko.ordering.product;

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
class Product {
    @Id
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

    static Product create(final String code, final String name, final double price){
       return new Product(code, name, price);
    }

    Product changePrice(final double price){
        this.price = price;
        return this;
    }

    Product addToStock(final int quantity) {
        this.quantity += quantity;
        return this;
    }

    Product removeFromStock(final int quantity) {
        if(this.quantity < quantity){
            throw new NotEnoughProductException(this.id);
        }

        this.quantity -= quantity;
        return this;
    }

    ProductDto dto() {
        return ProductDto
                    .builder()
                    .id(this.getId())
                    .name(this.getName())
                    .code(this.getCode())
                    .quantity(this.getQuantity())
                    .price(this.getPrice())
                    .build();
    }
}
