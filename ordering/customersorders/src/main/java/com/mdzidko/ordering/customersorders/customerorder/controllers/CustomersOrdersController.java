package com.mdzidko.ordering.customersorders.customerorder.controllers;

import com.mdzidko.ordering.customersorders.customerorder.domain.dto.CustomerOrderLineDto;
import com.mdzidko.ordering.customersorders.customerorder.domain.CustomersOrdersService;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.CustomerOrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/customersOrders")
class CustomersOrdersController {
    private final CustomersOrdersService customersOrdersService;

    CustomersOrdersController(final CustomersOrdersService customersOrdersService) {
        this.customersOrdersService = customersOrdersService;
    }

    @GetMapping
    public Iterable<CustomerOrderDto> findCustomersOrders(
            @RequestParam(value = "customerId", required = false) UUID customerId){

        Iterable<CustomerOrderDto> foundOrders;

        if(customerId == null){
            foundOrders = customersOrdersService.findAllOrders();
        }
        else{
            foundOrders = customersOrdersService.findAllCustomerOrders(customerId);
        }

        return foundOrders;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrderDto addNewCustomerOrder(@RequestBody String customerId){
        return customersOrdersService
                .createCustomerOrder(UUID.fromString(customerId));
    }

    @GetMapping("/{orderId}")
    public CustomerOrderDto findCustomersOrdersById(@PathVariable("orderId") UUID orderId){
        return customersOrdersService.findOrderById(orderId);
    }

    @GetMapping("/{orderId}/lines")
    public Iterable<CustomerOrderLineDto> findCustomerOrderLines(@PathVariable("orderId") UUID orderId){
        return customersOrdersService.findAllCustomerOrderLines(orderId);
    }

    @PostMapping("/{orderId}/lines")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrderDto addCustomerOrderLine(@PathVariable("orderId") UUID orderId,
                                                     @RequestBody NewOrderLineDto newOrderLineDto){
        return customersOrdersService
                .addProductToOrder(orderId, newOrderLineDto.getProductId(), newOrderLineDto.getQuantity());
    }

    @PutMapping("/{orderId}/cancelled")
    public CustomerOrderDto cancellCustomerOrder(@PathVariable("orderId") UUID orderId){
        return customersOrdersService.cancelOrder(orderId);
    }
}
