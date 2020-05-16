package com.mdzidko.ordering.repositories;

import com.mdzidko.ordering.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomersRepository {
    Optional<Customer> findById(UUID id);
    Iterable<Customer> findAll();
    Customer save(Customer customer);
}
