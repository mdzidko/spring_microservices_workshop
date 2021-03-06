package com.mdzidko.ordering.customersorders.customer;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.UUID;

public class CustomersService {
    private final RestTemplate restTemplate;

    public CustomersService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(
            include = {ConnectException.class, SocketTimeoutException.class, IllegalStateException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public boolean customerExists(final UUID customerId) {
        return restTemplate
                .getForEntity("/" + customerId.toString(), String.class)
                .getStatusCode() == HttpStatus.OK;
    }

    @Retryable(
            include = {ConnectException.class, SocketTimeoutException.class, IllegalStateException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void removeCreditsFromCustomer(final UUID customerId, final double credits) {
        restTemplate
                .exchange("/" + customerId.toString() + "/credits",
                    HttpMethod.DELETE,
                    new HttpEntity<>(credits),
                    String.class);
    }

    @Retryable(
            include = {ConnectException.class, SocketTimeoutException.class, IllegalStateException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void addCreditsForCustomer(final UUID customerId, final double credits) {
        restTemplate
                .exchange("/" + customerId.toString() + "/credits",
                        HttpMethod.PUT,
                        new HttpEntity<>(credits),
                        String.class);
    }
}
