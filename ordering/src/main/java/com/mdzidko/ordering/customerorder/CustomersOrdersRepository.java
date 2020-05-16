package com.mdzidko.ordering.customerorder;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface CustomersOrdersRepository extends CrudRepository<CustomerOrder, UUID> {
    Iterable<CustomerOrder> findAllByCustomerId(UUID customerId);
}
