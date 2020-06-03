package com.mdzidko.ordering.customers;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CustomersOrdersEventsSink {
    String INPUT = "customersOrdersEvents";

    @Input(INPUT)
    SubscribableChannel customersOrdersEvents();
}
