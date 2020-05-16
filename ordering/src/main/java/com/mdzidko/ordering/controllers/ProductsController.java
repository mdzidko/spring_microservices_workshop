package com.mdzidko.ordering.controllers;

import com.mdzidko.ordering.dtos.ProductDto;
import com.mdzidko.ordering.model.Product;
import com.mdzidko.ordering.services.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(final ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public Iterable<ProductDto> findAllProducts(){
        return StreamSupport
                .stream(productsService.findAllProducts().spliterator(), false)
                .map(Product::dto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{productId}")
    public ProductDto findProductById(@PathVariable("productId") UUID productId){
        return productsService.findProductById(productId).dto();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto addNewproduct(@RequestBody ProductDto productDto){
        return productsService
                .addNewProduct(
                        productDto.getName(),
                        productDto.getCode(),
                        productDto.getPrice())
                .dto();
    }

    @PutMapping("/{productId}/quantity")
    public ProductDto addQuantityForProduct(@PathVariable("productId") UUID productId, @RequestBody String quantity){
        return productsService.addProductToStock(productId, Integer.valueOf(quantity)).dto();
    }

    @DeleteMapping("/{productId}/quantity")
    public ProductDto removeQuantityForProduct(@PathVariable("productId") UUID productId, @RequestBody String quantity){
        return productsService.removeProductFromStock(productId, Integer.valueOf(quantity)).dto();
    }

}
