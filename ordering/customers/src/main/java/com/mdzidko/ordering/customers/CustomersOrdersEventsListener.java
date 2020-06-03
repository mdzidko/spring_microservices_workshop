package com.mdzidko.ordering.customers;

import com.mdzidko.ordering.customers.customer.CustomersService;
import com.mdzidko.ordering.customers.customerorder.event.CustomerOrderCanceledEvent;
import com.mdzidko.ordering.customers.customerorder.event.ProductAddedToCustomerOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@EnableBinding(CustomersOrdersEventsSink.class)
class CustomersOrdersEventsListener {
    private final CustomersService customersService;

    CustomersOrdersEventsListener(final CustomersService customersService) {
        this.customersService = customersService;
    }

    @StreamListener(value = CustomersOrdersEventsSink.INPUT, condition = "headers['type'] == 'PRODUCT_ADDED_TO_ORDER'")
    void onProductAddedToCustomerOrderEvent(Message<ProductAddedToCustomerOrderEvent> message){
        ProductAddedToCustomerOrderEvent event = message.getPayload();
        customersService.removeCreditsFromCustomer(event.getCustomerId(), event.getProductPrice() * event.getProductQuantity());
    }

    @StreamListener(value = CustomersOrdersEventsSink.INPUT, condition = "headers['type'] == 'ORDER_CANCELED'")
    void onCustomerOrderCanceledEvent(Message<CustomerOrderCanceledEvent> message){
        CustomerOrderCanceledEvent event = message.getPayload();
        customersService.addCreditsForCustomer(event.getCustomerId(), event.getValue());
    }
}
