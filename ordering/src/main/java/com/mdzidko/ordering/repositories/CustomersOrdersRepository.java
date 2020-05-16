package com.mdzidko.ordering.repositories;

import com.mdzidko.ordering.model.Customer;
import com.mdzidko.ordering.model.CustomerOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomersOrdersRepository extends CrudRepository<CustomerOrder, UUID> {
    Iterable<CustomerOrder> findAllByCustomer(Customer customer);
}
