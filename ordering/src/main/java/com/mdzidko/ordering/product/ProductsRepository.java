package com.mdzidko.ordering.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface ProductsRepository extends CrudRepository<Product, UUID> {
    boolean existsByCode(String code);
}
