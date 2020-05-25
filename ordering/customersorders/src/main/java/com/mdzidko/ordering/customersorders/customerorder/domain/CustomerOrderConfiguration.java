package com.mdzidko.ordering.customersorders.customerorder.domain;

import com.mdzidko.ordering.customersorders.customer.CustomersService;
import com.mdzidko.ordering.customersorders.product.ProductsService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class CustomerOrderConfiguration {
    private CustomersService customersService(RestTemplateBuilder restTemplateBuilder){
        return new CustomersService(restTemplateBuilder);
    }

    private ProductsService productsService(RestTemplateBuilder restTemplateBuilder) {
        return new ProductsService(restTemplateBuilder);
    }

    @Bean
    CustomersOrdersService customersOrdersService(CustomersOrdersRepository customersOrdersRepository,
                                                  RestTemplateBuilder restTemplateBuilder){
        return new CustomersOrdersService(customersOrdersRepository,
                                            customersService(restTemplateBuilder),
                                            productsService(restTemplateBuilder));
    }
}
