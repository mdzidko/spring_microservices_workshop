package com.mdzidko.ordering.model;

import com.mdzidko.ordering.dtos.ProductDto;
import com.mdzidko.ordering.exceptions.NotEnoughProductException;
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
public class Product {
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

    public ProductDto dto() {
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
