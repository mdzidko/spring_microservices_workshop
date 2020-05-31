package com.mdzidko.ordering.customersorders.product;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.UUID;


public class ProductsService {
    private final RestTemplate restTemplate;

    public ProductsService(final RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .rootUri("http://localhost:8082/products")
                .errorHandler(new ProductsResponseErrorHandler())
                .setConnectTimeout(Duration.ofMillis(1000))
                .build();
    }

    @Retryable(
            include = {ConnectException.class, SocketTimeoutException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public ProductDto findProductById(final UUID productId) {
        return restTemplate
                .getForObject("/" + productId.toString(), ProductDto.class);
    }

    @Retryable(
            include = {ConnectException.class, SocketTimeoutException.class},
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
            include = {ConnectException.class, SocketTimeoutException.class},
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
