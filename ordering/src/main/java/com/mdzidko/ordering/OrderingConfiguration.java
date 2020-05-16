package com.mdzidko.ordering;

import com.mdzidko.ordering.repositories.CustomersOrdersRepository;
import com.mdzidko.ordering.repositories.CustomersRepository;
import com.mdzidko.ordering.repositories.ProductsRepository;
import com.mdzidko.ordering.services.CustomersOrdersService;
import com.mdzidko.ordering.services.CustomersService;
import com.mdzidko.ordering.services.ProductsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class OrderingConfiguration {
    @Bean
    CustomersService customersService(CustomersRepository customersRepository){
        return new CustomersService(customersRepository);
    }

    @Bean
    ProductsService productsService(ProductsRepository productsRepository){
        return new ProductsService(productsRepository);
    }

    @Bean
    CustomersOrdersService customersOrdersService(CustomersOrdersRepository customersOrdersRepository,
                                                  CustomersRepository customersRepository,
                                                  ProductsRepository productsRepository){
        return new CustomersOrdersService(customersOrdersRepository, customersRepository, productsRepository);
    }
}
