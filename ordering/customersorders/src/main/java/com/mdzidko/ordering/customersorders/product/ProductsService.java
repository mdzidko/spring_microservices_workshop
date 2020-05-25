package com.mdzidko.ordering.customersorders.product;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public class ProductsService {
    private final RestTemplate restTemplate;

    public ProductsService(final RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .rootUri("http://localhost:8082/products")
                .errorHandler(new ProductsResponseErrorHandler())
                .build();
    }

    public ProductDto findProductById(final UUID productId) {
        return restTemplate
                .getForObject("/" + productId.toString(), ProductDto.class);
    }

    public void removeProductFromStock(final UUID productId, final int productQuantity) {
        restTemplate
                .exchange("/" + productId.toString() + "/quantity",
                        HttpMethod.DELETE,
                        new HttpEntity<>(productQuantity),
                        String.class);
    }

    public void addProductToStock(final UUID productId, final int productQuantity) {
        restTemplate
                .exchange("/" + productId.toString() + "/quantity",
                        HttpMethod.PUT,
                        new HttpEntity<>(productQuantity),
                        String.class);
    }
}
