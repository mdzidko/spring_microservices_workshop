package com.mdzidko.ordering.repositories;

import com.mdzidko.ordering.model.Customer;
import com.mdzidko.ordering.model.CustomerOrder;

import java.util.Optional;
import java.util.UUID;

public interface CustomersOrdersRepository {
    CustomerOrder save(CustomerOrder customerOrder);
    Iterable<CustomerOrder> findAll();
    Iterable<CustomerOrder> findAllByCustomer(Customer customer);
    Optional<CustomerOrder> findById(UUID orderId);
}
