package com.mdzidko.ordering.customersorders.customerorder.domain;

import com.mdzidko.ordering.customersorders.customer.CustomersService;
import com.mdzidko.ordering.customersorders.product.ProductsService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableRetry
class CustomerOrderConfiguration {
    @Bean
    @RefreshScope
    CustomersOrdersService customersOrdersService(CustomersOrdersRepository customersOrdersRepository,
                                                  CustomersService customersService,
                                                  ProductsService productsService){
        return new CustomersOrdersService(customersOrdersRepository,
                                            customersService,
                                            productsService);
    }
}
