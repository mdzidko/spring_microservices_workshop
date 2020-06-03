package com.mdzidko.ordering.customersorders.customerorder;

import com.mdzidko.ordering.customersorders.customerorder.domain.event.CustomerOrderCanceledEvent;
import com.mdzidko.ordering.customersorders.customerorder.domain.event.ProductAddedToCustomerOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class CustomerOrderEventsListener {
    @EventListener
    void onCustomerOrderCanceledEvent(CustomerOrderCanceledEvent event){
        log.info("Order {} was canceled", event.getOrderId());
    }

    @EventListener
    void onProductAddedToCustomerOrderEvent (ProductAddedToCustomerOrderEvent event){
        log.info("Product {} was added to order {}", event.getProductId(), event.getOrderId());
    }
}
