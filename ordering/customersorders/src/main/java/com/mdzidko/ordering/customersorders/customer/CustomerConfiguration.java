package com.mdzidko.ordering.customersorders.customer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
class CustomerConfiguration {
    @Bean("customersRestTemplate")
    @LoadBalanced
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder,
                              @Value("${customersService.name:customers}") String customersServiceName,
                              @Value("${customersService.customersEndpoint:customers}") String customersEndpoint){
        return restTemplateBuilder
                .rootUri("http://" + customersServiceName + "/" + customersEndpoint)
                .errorHandler(new CustomersResponseErrorHandler())
                .setConnectTimeout(Duration.ofMillis(1000))
                .build();
    }

    @Bean
    CustomersService customersService(@Qualifier("customersRestTemplate")RestTemplate restTemplate){
        return new CustomersService(restTemplate);
    }
}