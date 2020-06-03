package com.mdzidko.ordering.customersorders.customerorder;

import com.mdzidko.ordering.customersorders.CustomersOrdersEventsSource;
import com.mdzidko.ordering.customersorders.customerorder.domain.event.CustomerOrderCanceledEvent;
import com.mdzidko.ordering.customersorders.customerorder.domain.event.ProductAddedToCustomerOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.event.EventListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableBinding(CustomersOrdersEventsSource.class)
class CustomerOrderEventsListener {
    private final CustomersOrdersEventsSource eventsSource;

    CustomerOrderEventsListener(final CustomersOrdersEventsSource eventsSource) {
        this.eventsSource = eventsSource;
    }

    @EventListener
    void onCustomerOrderCanceledEvent(CustomerOrderCanceledEvent event){
        Message<CustomerOrderCanceledEvent> message = MessageBuilder
                .withPayload(event)
                .build();

        eventsSource.customersOrdersEvents().send(message);

        log.info("Order {} was canceled", event.getOrderId());
    }

    @EventListener
    void onProductAddedToCustomerOrderEvent (ProductAddedToCustomerOrderEvent event){
        Message<ProductAddedToCustomerOrderEvent> message = MessageBuilder
                .withPayload(event)
                .build();

        eventsSource.customersOrdersEvents().send(message);

        log.info("Product {} was added to order {}", event.getProductId(), event.getOrderId());
    }
}
