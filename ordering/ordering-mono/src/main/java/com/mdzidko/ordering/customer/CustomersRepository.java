package com.mdzidko.ordering.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface CustomersRepository extends CrudRepository<Customer, UUID> {
    boolean existsByNameAndSurname(String name, String surname);
}
