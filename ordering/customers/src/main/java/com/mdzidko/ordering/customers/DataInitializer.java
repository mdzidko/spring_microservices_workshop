package com.mdzidko.ordering.customers;

import com.mdzidko.ordering.customers.customer.CustomersService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final CustomersService customersService;

    DataInitializer(final CustomersService customersService) {
        this.customersService = customersService;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    private void initData(){
        initCustomers();

        printCollection(customersService.findAllCustomers(), "Customers");
    }

    private void initCustomers() {
        try {
            customersService
                    .addNewCustomer("Adrian", "Polityczny", "Wiejska", "Warszawa", "00-902", 4);
            customersService
                    .addNewCustomer("Lucjan", "Diaboliczny", "Piekielna", "Hel", "66-666", 666);

            customersService
                    .findAllCustomers()
                    .forEach(customer -> customersService.addCreditsForCustomer(customer.getId(), 1000));
        }
        catch(Exception ex){
            System.out.println("Customers already initialized");
        }
    }

    private void printCollection(Iterable<?> collection, String label){
        System.out.println(label + ": [");
        collection.forEach(System.out::println);
        System.out.println("] \n");
    }
}
