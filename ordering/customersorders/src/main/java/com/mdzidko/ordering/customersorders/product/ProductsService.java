package com.mdzidko.ordering.customersorders.product;

import java.util.UUID;

public class ProductsService {
    public ProductDto findProductById(final UUID productId) {
        return ProductDto
                .builder()
                .productId(UUID.randomUUID())
                .price(10.0)
                .build();
    }

    public void removeProductFromStock(final UUID productId, final int productQuantity) {

    }

    public void addProductToStock(final UUID productId, final int productQuantity) {

    }
}
