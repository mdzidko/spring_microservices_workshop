package com.mdzidko.ordering;

import com.mdzidko.ordering.services.CustomersService;
import com.mdzidko.ordering.services.ProductsService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final CustomersService customersService;
    private final ProductsService productsService;

    DataInitializer(final CustomersService customersService, final ProductsService productsService) {
        this.customersService = customersService;
        this.productsService = productsService;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    private void initData(){
        initCustomers();
        initProducts();

        printCollection(productsService.findAllProducts(), "Products");
        printCollection(customersService.findAllCustomers(), "Customers");
    }

    private void initCustomers() {
        customersService
                .addNewCustomer("Adrian", "Polityczny", "Wiejska", "Warszawa", "00-902", 4);
        customersService
                .addNewCustomer("Lucjan", "Diaboliczny", "Piekielna", "Hel", "66-666", 666);

        customersService
                .findAllCustomers()
                .forEach(customer -> customersService.addCreditsForCustomer(customer.getId(), 1000));
    }

    public void initProducts() {
        productsService.addNewProduct("12344321", "Bucket", 10.0);
        productsService.addNewProduct("98756632", "Scythe", 50.0);
        productsService.addNewProduct("43298854", "Chain", 20.0);
        productsService.addNewProduct("87566432", "Barrel", 100.0);

        productsService
                .findAllProducts()
                .forEach(product -> productsService.addProductToStock(product.getId(), 20));
    }

    private void printCollection(Iterable<?> collection, String label){
        System.out.println(label + ": [");
        collection.forEach(System.out::println);
        System.out.println("] \n");
    }
}
