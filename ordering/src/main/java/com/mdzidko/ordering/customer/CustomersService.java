package com.mdzidko.ordering.customer;

import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CustomersService {
    private final CustomersRepository customersRepository;

    public CustomersService(final CustomersRepository customersRepository) {
        this.customersRepository = customersRepository;
    }

    public CustomerDto addNewCustomer(String name, String surname, String street, String city, String postal, int local){
        if(customersRepository.existsByNameAndSurname(name, surname)){
            throw new CustomerAlreadyExistsException(name, surname);
        }

        Address address = Address.create(street, city, postal, local);

        Customer customer =  Customer.create(name, surname, address);
        return customersRepository.save(customer).dto();
    }

    public Iterable<CustomerDto> findAllCustomers(){
        return StreamSupport
                .stream(customersRepository.findAll().spliterator(), false)
                .map(Customer::dto)
                .collect(Collectors.toList());
    }

    public CustomerDto findCustomerById(final UUID customerId){
        return customersRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId))
                .dto();
    }

    @Transactional
    public CustomerDto addCreditsForCustomer(final UUID customerId, final double credits) {
        Customer customer = customersRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return customer.addCredits(credits).dto();
    }

    @Transactional
    public CustomerDto removeCreditsFromCustomer(final UUID customerId, final double credits) {
        Customer customer = customersRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return customer.reserveCredits(credits).dto();
    }
}
