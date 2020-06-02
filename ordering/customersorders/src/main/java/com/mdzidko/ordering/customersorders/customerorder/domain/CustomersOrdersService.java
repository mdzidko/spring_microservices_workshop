package com.mdzidko.ordering.customersorders.customerorder.domain;

import com.mdzidko.ordering.customersorders.customer.CustomersService;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.BadOrderStatusException;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.CustomerOrderDto;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.CustomerOrderLineDto;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.OrderNotFoundException;
import com.mdzidko.ordering.customersorders.product.ProductDto;
import com.mdzidko.ordering.customersorders.product.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.retry.annotation.CircuitBreaker;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
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
    public CustomerOrderDto addProductToOrder(final UUID orderId, final UUID productId, final int productQuantity){
        ProductDto product = productsService.findProductById(productId);
        double productPrice = product.getPrice();

        CustomerOrder customerOrder = createNewOrderLine(orderId, productId, productQuantity, productPrice);

        try {
            productsService.removeProductFromStock(productId, productQuantity);
        }
        catch(Exception ex){
            removeProductFromOrder(orderId, productId, productQuantity);
            throw ex;
        }

        try{
            customersService.removeCreditsFromCustomer(customerOrder.getCustomerId(), productQuantity * productPrice );
        }
        catch(Exception ex){
            productsService.addProductToStock(productId, productQuantity);
            removeProductFromOrder(orderId, productId, productQuantity);
            throw ex;
        }

        log.debug("Added " + productQuantity + " products with id = " + productId + " to order " + orderId);

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
    public CustomerOrderDto cancelOrder(final UUID orderId){
        CustomerOrder customerOrder = customersOrdersRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if(customerOrder.getStatus() == CustomerOrderStatus.CANCELLED){
            throw new BadOrderStatusException(customerOrder.getStatus().toString());
        }

        double orderPrice = customerOrder.calculateOrderValue();

        customerOrder.cancel();
        customersOrdersRepository.save(customerOrder);

        try {
            addQuantityForAllOrderLinesProducts(customerOrder);
        }
        catch(Exception ex){
            customerOrder.setAsNew();
            customersOrdersRepository.save(customerOrder);
            throw ex;
        }

        try {
            customersService.addCreditsForCustomer(customerOrder.getCustomerId(), orderPrice);
        }
        catch(Exception ex){
            customerOrder.setAsNew();
            customersOrdersRepository.save(customerOrder);
            removeQuantityForAllOrderLinesProducts(customerOrder);
            throw ex;
        }

        log.debug("Cancelled order  " + orderId);

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

    private void removeQuantityForAllOrderLinesProducts(final CustomerOrder customerOrder){
        customerOrder
                .getLines()
                .forEach(orderLine -> {
                    int productQuantity = orderLine.getQuantity();
                    productsService.removeProductFromStock(orderLine.getProductId(), productQuantity);
                });
    }

    private void removeProductFromOrder(final UUID orderId,
                                        final UUID productId,
                                        final int productQuantity) {

        CustomerOrder customerOrder = customersOrdersRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        customersOrdersRepository.save(
                customerOrder.removeProduct(productId, productQuantity));
    }

    private CustomerOrder createNewOrderLine(final UUID orderId,
                                             final UUID productId,
                                             final int productQuantity,
                                             final double productPrice) {

        CustomerOrder customerOrder = customersOrdersRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if(customerOrder.getStatus() != CustomerOrderStatus.NEW){
            throw new BadOrderStatusException(customerOrder.getStatus().toString());
        }

        return customersOrdersRepository.save(
                customerOrder.addNewLine(productId, productQuantity, productPrice));
    }
}
