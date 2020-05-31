package com.mdzidko.ordering.customersorders.customerorder.domain;

import com.mdzidko.ordering.customersorders.customer.CustomersService;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.BadOrderStatusException;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.CustomerOrderDto;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.OrderNotFoundException;
import com.mdzidko.ordering.customersorders.product.ProductDto;
import com.mdzidko.ordering.customersorders.product.ProductsService;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.transaction.annotation.Transactional;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
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

    @CircuitBreaker(
            maxAttempts = 2,
            resetTimeout = 10000,
            openTimeout = 10000,
            include = {ConnectException.class,
                    SocketTimeoutException.class,
                    JDBCConnectionException.class}
    )
    public CustomerOrderDto createCustomerOrder(final UUID customerId){
        customersService.customerExists(customerId);
        return customersOrdersRepository.save(CustomerOrder.create(customerId)).dto();
    }

    @CircuitBreaker(
            maxAttempts = 1,
            resetTimeout = 10000,
            include = {JDBCConnectionException.class}
    )
    public Iterable<CustomerOrderDto> findAllOrders(){
        return StreamSupport
                .stream(customersOrdersRepository.findAll().spliterator(), false)
                .map(CustomerOrder::dto)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(
            maxAttempts = 2,
            resetTimeout = 10000,
            openTimeout = 10000,
            include = {ConnectException.class,
                    SocketTimeoutException.class,
                    JDBCConnectionException.class}
    )
    public Iterable<CustomerOrderDto> findAllCustomerOrders(final UUID customerId){
        customersService.customerExists(customerId);

        return StreamSupport
                .stream(customersOrdersRepository.findAllByCustomerId(customerId).spliterator(), false)
                .map(CustomerOrder::dto)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(
            maxAttempts = 1,
            resetTimeout = 10000,
            include = {JDBCConnectionException.class}
    )
    public CustomerOrderDto findOrderById(final UUID orderId){
        return customersOrdersRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId))
                .dto();
    }

    @CircuitBreaker(
            maxAttempts = 2,
            resetTimeout = 10000,
            openTimeout = 10000,
            include = {ConnectException.class,
                    SocketTimeoutException.class,
                    JDBCConnectionException.class}
    )
    @Transactional
    public CustomerOrderDto addProductToOrder(final UUID orderId, final UUID productId, final int productQuantity){
        CustomerOrder customerOrder = customersOrdersRepository
                                        .findById(orderId)
                                        .orElseThrow(() -> new OrderNotFoundException(orderId));

        if(customerOrder.getStatus() != CustomerOrderStatus.NEW){
            throw new BadOrderStatusException(customerOrder.getStatus().toString());
        }

        ProductDto product = productsService.findProductById(productId);
        double productPrice = product.getPrice();

        customerOrder.addNewLine(productId, productQuantity, productPrice);

        productsService.removeProductFromStock(productId, productQuantity);

        try{
            customersService.removeCreditsFromCustomer(customerOrder.getCustomerId(), productQuantity * productPrice );
        }
        catch(Exception ex){
            productsService.addProductToStock(productId, productQuantity);
            throw ex;
        }

        return customerOrder.dto();
    }

    @CircuitBreaker(
            maxAttempts = 2,
            resetTimeout = 10000,
            openTimeout = 10000,
            include = {ConnectException.class,
                    SocketTimeoutException.class,
                    JDBCConnectionException.class}
    )
    @Transactional
    public CustomerOrderDto cancelOrder(final UUID orderId){
        CustomerOrder customerOrder = customersOrdersRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if(customerOrder.getStatus() == CustomerOrderStatus.CANCELLED){
            throw new BadOrderStatusException(customerOrder.getStatus().toString());
        }

        customerOrder.cancel();

        addQuantityForAllOrderLinesProducts(customerOrder);

        double orderPrice = customerOrder.calculateOrderValue();
        customersService.addCreditsForCustomer(customerOrder.getCustomerId(), orderPrice);

        return customerOrder.dto();
    }

    @CircuitBreaker(
            maxAttempts = 1,
            resetTimeout = 10000,
            include = {JDBCConnectionException.class}
    )
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
