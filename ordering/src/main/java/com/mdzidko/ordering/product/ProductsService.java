package com.mdzidko.ordering.product;

import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ProductsService {
    private final ProductsRepository productsRepository;

    public ProductsService(final ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Product addNewProduct(String code, String name, double price){
        if(productsRepository.existsByCode(code))
            throw new ProductExistsException(code);

        Product product = Product.create(code, name, price);

        return productsRepository.save(product);
    }

    public Iterable<Product> findAllProducts(){
        return StreamSupport
                    .stream(productsRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
    }

    public Product findProductById(UUID productId){
        return productsRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Product addProductToStock(UUID productId, int quantity){
        Product product = productsRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return product.addToStock(quantity);
    }

    public Product removeProductFromStock(UUID productId, int quantity){
        Product product = findProductById(productId);
        return product.removeFromStock(quantity);
    }
}
