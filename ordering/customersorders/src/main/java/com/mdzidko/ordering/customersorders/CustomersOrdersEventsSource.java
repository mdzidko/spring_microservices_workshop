package com.mdzidko.ordering.customersorders;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CustomersOrdersEventsSource {
    String OUTPUT = "customersOrdersEvents";

    @Output(OUTPUT)
    MessageChannel customersOrdersEvents();
}
