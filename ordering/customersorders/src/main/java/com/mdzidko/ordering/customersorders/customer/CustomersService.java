package com.mdzidko.ordering.customersorders.customer;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public class CustomersService {
    private final RestTemplate restTemplate;

    public CustomersService(final RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .rootUri("http://localhost:8083/customers")
                .errorHandler(new CustomersResponseErrorHandler())
                .build();
    }

    public boolean customerExists(final UUID customerId) {
        return restTemplate
                    .getForEntity("/" + customerId.toString(), String.class)
                    .getStatusCode() == HttpStatus.OK;
    }

    public void removeCreditsFromCustomer(final UUID customerId, final double v) {

    }

    public void addCreditsForCustomer(final UUID customerId, final double orderPrice) {

    }
}
