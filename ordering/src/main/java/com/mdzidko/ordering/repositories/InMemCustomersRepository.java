package com.mdzidko.ordering.repositories;

import com.mdzidko.ordering.model.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemCustomersRepository implements CustomersRepository{
    private final Map<UUID, Customer> customers = new HashMap<>();

    @Override
    public Optional<Customer> findById(final UUID id) {
        return Optional.ofNullable(customers.getOrDefault(id, null));
    }

    @Override
    public Iterable<Customer> findAll() {
        return customers.values();
    }

    @Override
    public Customer save(final Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }
}
