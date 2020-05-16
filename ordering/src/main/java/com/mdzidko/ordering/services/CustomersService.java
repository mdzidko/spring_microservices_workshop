package com.mdzidko.ordering.services;

import com.mdzidko.ordering.exceptions.CustomerAlreadyExistsException;
import com.mdzidko.ordering.exceptions.CustomerNotFoundException;
import com.mdzidko.ordering.model.Address;
import com.mdzidko.ordering.model.Customer;
import com.mdzidko.ordering.repositories.CustomersRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class CustomersService {
    private final CustomersRepository customersRepository;

    public CustomersService(final CustomersRepository customersRepository) {
        this.customersRepository = customersRepository;
    }

    public Customer addNewCustomer(String name, String surname, String street, String city, String postal, int local){
        if(customersRepository.existsByNameAndSurname(name, surname)){
            throw new CustomerAlreadyExistsException(name, surname);
        }

        Address address = Address.create(street, city, postal, local);

        Customer customer =  Customer.create(name, surname, address);
        return customersRepository.save(customer);
    }

    public Iterable<Customer> findAllCustomers(){
        return customersRepository.findAll();
    }

    public Customer findCustomerById(final UUID id){
        return customersRepository
                .findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional
    public Customer addCreditsForCustomer(final UUID customerId, final double credits) {
        Customer customer = findCustomerById(customerId);
        return customer.addCredits(credits);
    }

    @Transactional
    public Customer removeCreditsFromCustomer(final UUID customerId, final double credits) {
        Customer customer = findCustomerById(customerId);
        return customer.reserveCredits(credits);
    }
}
