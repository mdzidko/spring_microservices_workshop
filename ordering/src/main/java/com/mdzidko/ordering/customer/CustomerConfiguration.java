package com.mdzidko.ordering.customer;


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