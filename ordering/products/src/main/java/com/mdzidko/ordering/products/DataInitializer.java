package com.mdzidko.ordering.products;

import com.mdzidko.ordering.products.domain.ProductsService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final ProductsService productsService;

    DataInitializer(final ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    private void initData(){
        initProducts();

        printCollection(productsService.findAllProducts(), "Products");
    }

    public void initProducts() {
        try {
            productsService.addNewProduct("12344321", "Bucket", 10.0);
            productsService.addNewProduct("98756632", "Scythe", 50.0);
            productsService.addNewProduct("43298854", "Chain", 20.0);
            productsService.addNewProduct("87566432", "Barrel", 100.0);

            productsService
                    .findAllProducts()
                    .forEach(product -> productsService.addProductToStock(product.getId(), 20));
        }
        catch(Exception ex){
            System.out.println("Products already initialized");
        }
    }

    private void printCollection(Iterable<?> collection, String label){
        System.out.println(label + ": [");
        collection.forEach(System.out::println);
        System.out.println("] \n");
    }
}
