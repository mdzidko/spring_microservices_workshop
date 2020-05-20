package com.mdzidko.ordering.customerorder;

import com.mdzidko.ordering.customers.customer.CustomersService;
import com.mdzidko.ordering.products.product.ProductsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class CustomerOrderConfiguration {
    @Bean
    CustomersOrdersService customersOrdersService(CustomersOrdersRepository customersOrdersRepository,
                                                  CustomersService customersService,
                                                  ProductsService productsService){
        return new CustomersOrdersService(customersOrdersRepository, customersService, productsService);
    }
}
