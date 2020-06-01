package com.mdzidko.ordering.customersorders.product;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.UUID;


public class ProductsService {
    private final RestTemplate restTemplate;

    public ProductsService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(
            include = {ConnectException.class, SocketTimeoutException.class, IllegalStateException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public ProductDto findProductById(final UUID productId) {
        return restTemplate
                .getForObject("/" + productId.toString(), ProductDto.class);
    }

    @Retryable(
            include = {ConnectException.class, SocketTimeoutException.class, IllegalStateException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void removeProductFromStock(final UUID productId, final int productQuantity) {
        restTemplate
                .exchange("/" + productId.toString() + "/quantity",
                        HttpMethod.DELETE,
                        new HttpEntity<>(productQuantity),
                        String.class);
    }

    @Retryable(
            include = {ConnectException.class, SocketTimeoutException.class, IllegalStateException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void addProductToStock(final UUID productId, final int productQuantity) {
        restTemplate
                .exchange("/" + productId.toString() + "/quantity",
                        HttpMethod.PUT,
                        new HttpEntity<>(productQuantity),
                        String.class);
    }
}
