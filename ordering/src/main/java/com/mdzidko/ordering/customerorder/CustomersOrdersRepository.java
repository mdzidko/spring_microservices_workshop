package com.mdzidko.ordering.customerorder;

import com.mdzidko.ordering.customer.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomersOrdersRepository extends CrudRepository<CustomerOrder, UUID> {
    Iterable<CustomerOrder> findAllByCustomer(Customer customer);
}
