package com.mdzidko.ordering.products.controllers;

import com.mdzidko.ordering.products.domain.dto.ProductDto;
import com.mdzidko.ordering.products.domain.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
class ProductsController {
    private final ProductsService productsService;

    public ProductsController(final ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public Iterable<ProductDto> findAllProducts(){
        return productsService.findAllProducts();
    }

    @GetMapping("/{productId}")
    public ProductDto findProductById(@PathVariable("productId") UUID productId){
        return productsService.findProductById(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto addNewproduct(@RequestBody ProductDto productDto){
        return productsService
                .addNewProduct(
                        productDto.getName(),
                        productDto.getCode(),
                        productDto.getPrice());
    }

    @PutMapping("/{productId}/quantity")
    public ProductDto addQuantityForProduct(@PathVariable("productId") UUID productId, @RequestBody String quantity){
        return productsService.addProductToStock(productId, Integer.valueOf(quantity));
    }

    @DeleteMapping("/{productId}/quantity")
    public ProductDto removeQuantityForProduct(@PathVariable("productId") UUID productId, @RequestBody String quantity){
        return productsService.removeProductFromStock(productId, Integer.valueOf(quantity));
    }

}
