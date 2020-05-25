package com.mdzidko.ordering.customersorders.customerorder.domain;

import com.mdzidko.ordering.customersorders.customer.CustomersService;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.BadOrderStatusException;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.CustomerOrderDto;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.OrderNotFoundException;
import com.mdzidko.ordering.customersorders.product.ProductDto;
import com.mdzidko.ordering.customersorders.product.ProductsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CustomersOrdersService {
    private final CustomersOrdersRepository customersOrdersRepository;
    private final CustomersService customersService;
    private final ProductsService productsService;

    public CustomersOrdersService(final CustomersOrdersRepository customersOrdersRepository,
                                  final CustomersService customersService, final ProductsService productsService) {
        this.customersOrdersRepository = customersOrdersRepository;
        this.customersService = customersService;
        this.productsService = productsService;
    }


    public CustomerOrderDto createCustomerOrder(final UUID customerId){
        customersService.customerExists(customerId);
        return customersOrdersRepository.save(CustomerOrder.create(customerId)).dto();
    }

    public Iterable<CustomerOrderDto> findAllOrders(){
        return StreamSupport
                .stream(customersOrdersRepository.findAll().spliterator(), false)
                .map(CustomerOrder::dto)
                .collect(Collectors.toList());
    }

    public Iterable<CustomerOrderDto> findAllCustomerOrders(final UUID customerId){
        customersService.customerExists(customerId);

        return StreamSupport
                .stream(customersOrdersRepository.findAllByCustomerId(customerId).spliterator(), false)
                .map(CustomerOrder::dto)
                .collect(Collectors.toList());
    }

    public CustomerOrderDto findOrderById(final UUID orderId){
        return customersOrdersRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId))
                .dto();
    }

    public CustomerOrderDto addProductToOrder(final UUID orderId, final UUID productId, final int productQuantity){
        CustomerOrder customerOrder = customersOrdersRepository
                                        .findById(orderId)
                                        .orElseThrow(() -> new OrderNotFoundException(orderId));

        if(customerOrder.getStatus() != CustomerOrderStatus.NEW){
            throw new BadOrderStatusException(customerOrder.getStatus().toString());
        }

        ProductDto product = productsService.findProductById(productId);
        productsService.removeProductFromStock(productId, productQuantity);

        double productPrice = product.getPrice();
        customersService.removeCreditsFromCustomer(customerOrder.getCustomerId(), productQuantity * productPrice );

        return customerOrder
                .addNewLine(productId, productQuantity, productPrice )
                .dto();
    }

    public CustomerOrderDto cancelOrder(final UUID orderId){
        CustomerOrder customerOrder = customersOrdersRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if(customerOrder.getStatus() == CustomerOrderStatus.CANCELLED){
            throw new BadOrderStatusException(customerOrder.getStatus().toString());
        }

        addQuantityForAllOrderLinesProducts(customerOrder);

        double orderPrice = customerOrder.calculateOrderValue();
        customersService.addCreditsForCustomer(customerOrder.getCustomerId(), orderPrice);

        return customerOrder.cancel().dto();
    }

    public Iterable<CustomerOrderLineDto> findAllCustomerOrderLines(final UUID orderId) {
        return customersOrdersRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId))
                .getLines()
                .stream()
                .map(CustomerOrderLine::dto)
                .collect(Collectors.toList());
    }

    private void addQuantityForAllOrderLinesProducts(final CustomerOrder customerOrder){
        customerOrder
                .getLines()
                .forEach(orderLine -> {
                    int productQuantity = orderLine.getQuantity();
                    productsService.addProductToStock(orderLine.getProductId(), productQuantity);
                });
    }
}
