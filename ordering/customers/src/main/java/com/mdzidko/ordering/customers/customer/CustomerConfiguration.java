package com.mdzidko.ordering.customers.customer;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class CustomerConfiguration {
    @Bean
    CustomersService customersService(CustomersRepository customersRepository){
        return new CustomersService(customersRepository);
    }
}