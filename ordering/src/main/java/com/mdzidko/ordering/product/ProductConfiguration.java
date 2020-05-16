package com.mdzidko.ordering.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class ProductConfiguration {
    @Bean
    ProductsService productsService(ProductsRepository productsRepository){
        return new ProductsService(productsRepository);
    }
}
