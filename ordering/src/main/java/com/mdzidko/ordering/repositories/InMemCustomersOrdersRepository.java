package com.mdzidko.ordering.repositories;

import com.mdzidko.ordering.model.Customer;
import com.mdzidko.ordering.model.CustomerOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemCustomersOrdersRepository implements CustomersOrdersRepository{
    private final Map<UUID, CustomerOrder> customersOrders = new HashMap<>();

    @Override
    public CustomerOrder save(final CustomerOrder customerOrder) {
        customersOrders.put(customerOrder.getId(), customerOrder);
        return customerOrder;
    }

    @Override
    public Iterable<CustomerOrder> findAll() {
        return customersOrders.values();
    }

    @Override
    public Iterable<CustomerOrder> findAllByCustomer(final Customer customer) {
        return customersOrders
                    .values()
                    .stream()
                    .filter(customerOrder -> customerOrder.getCustomer() == customer)
                    .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerOrder> findById(final UUID orderId) {
        return Optional.ofNullable(customersOrders.getOrDefault(orderId, null));
    }
}
