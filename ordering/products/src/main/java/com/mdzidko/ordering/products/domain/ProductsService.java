package com.mdzidko.ordering.products.domain;

import com.mdzidko.ordering.products.domain.dto.ProductDto;
import com.mdzidko.ordering.products.domain.dto.ProductExistsException;
import com.mdzidko.ordering.products.domain.dto.ProductNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ProductsService {
    private final ProductsRepository productsRepository;

    public ProductsService(final ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public ProductDto addNewProduct(String code, String name, double price){
        if(productsRepository.existsByCode(code))
            throw new ProductExistsException(code);

        Product product = Product.create(code, name, price);

        return productsRepository.save(product).dto();
    }

    public Iterable<ProductDto> findAllProducts(){
        return StreamSupport
                    .stream(productsRepository.findAll().spliterator(), false)
                    .map(Product::dto)
                    .collect(Collectors.toList());
    }

    public ProductDto findProductById(UUID productId){
        return productsRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId))
                .dto();
    }

    public ProductDto addProductToStock(UUID productId, int quantity){
        Product product = productsRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return product.addToStock(quantity).dto();
    }

    public ProductDto removeProductFromStock(UUID productId, int quantity){
        Product product = productsRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return product.removeFromStock(quantity).dto();
    }
}
