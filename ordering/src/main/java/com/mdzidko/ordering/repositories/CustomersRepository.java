package com.mdzidko.ordering.repositories;

import com.mdzidko.ordering.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomersRepository extends CrudRepository<Customer, UUID> {
}
