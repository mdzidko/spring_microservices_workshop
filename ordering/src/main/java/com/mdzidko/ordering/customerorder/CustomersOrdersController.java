package com.mdzidko.ordering.customerorder;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/customersOrders")
public class CustomersOrdersController {
    private final CustomersOrdersService customersOrdersService;

    CustomersOrdersController(final CustomersOrdersService customersOrdersService) {
        this.customersOrdersService = customersOrdersService;
    }

    @GetMapping
    public Iterable<CustomerOrderDto> findCustomersOrders(
            @RequestParam(value = "customerId", required = false) UUID customerId){

        Iterable<CustomerOrder> foundOrders;

        if(customerId == null){
            foundOrders = customersOrdersService.findAllOrders();
        }
        else{
            foundOrders = customersOrdersService.findAllCustomerOrders(customerId);
        }

        return StreamSupport
                .stream(foundOrders.spliterator(), false)
                .map(CustomerOrder::dto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrderDto addNewCustomerOrder(@RequestBody String customerId){
        return customersOrdersService
                .createCustomerOrder(UUID.fromString(customerId))
                .dto();
    }

    @GetMapping("/{orderId}")
    public CustomerOrderDto findCustomersOrdersById(@PathVariable("orderId") UUID orderId){
        return customersOrdersService.findOrderById(orderId).dto();
    }

    @GetMapping("/{orderId}/lines")
    public Iterable<CustomerOrderLineDto> findCustomerOrderLines(@PathVariable("orderId") UUID orderId){
        return StreamSupport
                .stream(customersOrdersService.findAllCustomerOrderLines(orderId).spliterator(), false)
                .map(CustomerOrderLine::dto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{orderId}/lines")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrderDto addCustomerOrderLine(@PathVariable("orderId") UUID orderId,
                                                     @RequestBody NewOrderLineDto newOrderLineDto){
        return customersOrdersService
                .addProductToOrder(orderId, newOrderLineDto.getProductId(), newOrderLineDto.getQuantity())
                .dto();
    }

    @PutMapping("/{orderId}/cancelled")
    public CustomerOrderDto cancellCustomerOrder(@PathVariable("orderId") UUID orderId){
        return customersOrdersService.cancelOrder(orderId).dto();
    }
}
