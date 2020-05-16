package com.mdzidko.ordering.customer;

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

    public Customer addCreditsForCustomer(final UUID customerId, final double credits) {
        Customer customer = findCustomerById(customerId);
        return customer.addCredits(credits);
    }

    public Customer removeCreditsFromCustomer(final UUID customerId, final double credits) {
        Customer customer = findCustomerById(customerId);
        return customer.reserveCredits(credits);
    }
}
