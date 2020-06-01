package com.mdzidko.ordering.customersorders.product;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
class ProductConfiguration {
    @Bean("productsRestTemplate")
    @LoadBalanced
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder,
                              @Value("${productsService.name:products}") String productsServiceName,
                              @Value("${productsService.productsEndpoint:products}") String productsEndpoint){
        return restTemplateBuilder
                .rootUri("http://" + productsServiceName + "/" + productsEndpoint)
                .errorHandler(new ProductsResponseErrorHandler())
                .setConnectTimeout(Duration.ofMillis(1000))
                .build();
    }

    @Bean
    ProductsService productsService(@Qualifier("productsRestTemplate") RestTemplate restTemplate){
        return new ProductsService(restTemplate);
    }
}
