package com.mdzidko.ordering.customerorder;

import com.mdzidko.ordering.customer.CustomersRepository;
import com.mdzidko.ordering.product.ProductsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class CustomerOrderConfiguration {
    @Bean
    CustomersOrdersService customersOrdersService(CustomersOrdersRepository customersOrdersRepository,
                                                  CustomersRepository customersRepository,
                                                  ProductsRepository productsRepository){
        return new CustomersOrdersService(customersOrdersRepository, customersRepository, productsRepository);
    }
}
