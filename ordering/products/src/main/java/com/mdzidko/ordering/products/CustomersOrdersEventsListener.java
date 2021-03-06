package com.mdzidko.ordering.products;

import com.mdzidko.ordering.customerorder.dto.CustomerOrderLineDto;
import com.mdzidko.ordering.customerorder.event.CustomerOrderCanceledEvent;
import com.mdzidko.ordering.customerorder.event.ProductAddedToCustomerOrderEvent;
import com.mdzidko.ordering.products.domain.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@EnableBinding(CustomersOrdersEventsSink.class)
class CustomersOrdersEventsListener {
    private final ProductsService productsService;

    CustomersOrdersEventsListener(final ProductsService productsService) {
        this.productsService = productsService;
    }

    @StreamListener(value = CustomersOrdersEventsSink.INPUT, condition = "headers['type'] == 'PRODUCT_ADDED_TO_ORDER'")
    void onProductAddedToCustomerOrderEvent(Message<ProductAddedToCustomerOrderEvent> message){
        ProductAddedToCustomerOrderEvent event = message.getPayload();
        productsService.removeProductFromStock(event.getProductId(), event.getProductQuantity());
    }

    @StreamListener(value = CustomersOrdersEventsSink.INPUT, condition = "headers['type'] == 'ORDER_CANCELED'")
    void onCustomerOrderCanceledEvent(Message<CustomerOrderCanceledEvent> message){
        CustomerOrderCanceledEvent event = message.getPayload();
        Collection<CustomerOrderLineDto> lines = event.getLines();

        lines.forEach(line ->
                productsService.addProductToStock(line.getProductId(), line.getProductQuantity()));
    }
}
