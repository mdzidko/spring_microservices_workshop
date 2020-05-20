package com.mdzidko.ordering.customersorders.customerorder.domain;

import com.mdzidko.ordering.customersorders.customer.CustomersService;
import com.mdzidko.ordering.customersorders.product.ProductsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class CustomerOrderConfiguration {
    private CustomersService customersService(){
        return new CustomersService();
    }

    private ProductsService productsService() {
        return new ProductsService();
    }

    @Bean
    CustomersOrdersService customersOrdersService(CustomersOrdersRepository customersOrdersRepository){
        return new CustomersOrdersService(customersOrdersRepository, customersService(), productsService());
    }
}
