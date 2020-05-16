package com.mdzidko.ordering.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
class CustomersController {
    private final CustomersService customersService;

    public CustomersController(final CustomersService customersService) {
        this.customersService = customersService;
    }

    @GetMapping
    public Iterable<CustomerDto> findAllCustomers(){
        return customersService.findAllCustomers();
    }

    @GetMapping("/{customerId}")
    public CustomerDto findCustomerById(@PathVariable("customerId") UUID customerId){
        return customersService.findCustomerById(customerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto addNewCustomer(@RequestBody CustomerDto customerDto){
        return customersService
                .addNewCustomer(
                        customerDto.getName(),
                        customerDto.getSurname(),
                        customerDto.getStreet(),
                        customerDto.getCity(),
                        customerDto.getPostalCode(),
                        customerDto.getLocal());
    }

    @PutMapping("/{customerId}/credits")
    public CustomerDto addCreditsForCustomer(@PathVariable("customerId") UUID customerId, @RequestBody String credits){
        return customersService.addCreditsForCustomer(customerId, Double.valueOf(credits));
    }

    @DeleteMapping("/{customerId}/credits")
    public CustomerDto removeCreditsForCustomer(@PathVariable("customerId") UUID customerId, @RequestBody String credits){
        return customersService.removeCreditsFromCustomer(customerId, Double.valueOf(credits));
    }


}
