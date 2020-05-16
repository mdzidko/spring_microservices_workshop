package com.mdzidko.ordering.repositories;

import com.mdzidko.ordering.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductsRepository extends CrudRepository<Product, UUID> {
    boolean existsByCode(String code);
}
