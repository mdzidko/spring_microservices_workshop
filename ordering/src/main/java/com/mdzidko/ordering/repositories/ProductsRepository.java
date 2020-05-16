package com.mdzidko.ordering.repositories;

import com.mdzidko.ordering.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductsRepository {
    boolean existsByCode(String code);
    Product save(Product product);
    Iterable<Product> findAll();
    Optional<Product> findById(UUID productId);
}
