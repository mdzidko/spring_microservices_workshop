package com.mdzidko.ordering.repositories;

import com.mdzidko.ordering.model.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InMemProductsRepository implements ProductsRepository {
    private final Map<UUID, Product> products = new HashMap<>();

    @Override
    public boolean existsByCode(final String code) {
        return products
                    .values()
                    .stream()
                    .anyMatch(product -> product.getCode().equals(code));
    }

    @Override
    public Product save(final Product product) {
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public Iterable<Product> findAll() {
        return products.values();
    }

    @Override
    public Optional<Product> findById(final UUID productId) {
        return Optional.ofNullable(products.getOrDefault(productId, null));
    }
}
