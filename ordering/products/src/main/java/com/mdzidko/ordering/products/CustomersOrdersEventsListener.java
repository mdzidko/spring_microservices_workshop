package com.mdzidko.ordering.products;

import com.mdzidko.ordering.customerorder.event.CustomerOrderCanceledEvent;
import com.mdzidko.ordering.customerorder.event.ProductAddedToCustomerOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(CustomersOrdersEventsSink.class)
class CustomersOrdersEventsListener {
    @StreamListener(value = CustomersOrdersEventsSink.INPUT, condition = "headers['type'] == 'PRODUCT_ADDED_TO_ORDER'")
    void onProductAddedToCustomerOrderEvent(Message<ProductAddedToCustomerOrderEvent> event){
        log.info("Product {} was added to order {}", event.getPayload().getProductId(), event.getPayload().getOrderId());
    }

    @StreamListener(value = CustomersOrdersEventsSink.INPUT, condition = "headers['type'] == 'ORDER_CANCELED'")
    void onCustomerOrderCanceledEvent(Message<CustomerOrderCanceledEvent> event){
        log.info("Order {} was canceled", event.getPayload().getOrderId());
    }
}
