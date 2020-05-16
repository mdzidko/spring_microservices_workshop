package com.mdzidko.ordering.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class CustomerOrder {
    private UUID id;
    private Customer customer;
    private List<CustomerOrderLine> lines = new ArrayList<>();
    private CustomerOrderStatus status = CustomerOrderStatus.NEW;

    private CustomerOrder(final Customer customer) {
        this.id = UUID.randomUUID();
        this.customer = customer;
    }

    public static CustomerOrder create(final Customer customer){
        return new CustomerOrder(customer);
    }

    public double calculateOrderValue() {
        return lines
                .stream()
                .map(CustomerOrderLine::calculateValue)
                .reduce(0.0, (sum, lineValue) -> sum + lineValue);
    }

    public CustomerOrder addNewLine(final Product product, final int productQuantity) {
        lines
                .stream()
                .filter(orderLine -> orderLine.getProduct() == product)
                .findFirst()
                .ifPresentOrElse(
                        line -> line.addQuantity(productQuantity),
                        () -> lines.add(CustomerOrderLine.create(product, productQuantity)));

        return this;
    }

    public void cancel() {
        this.status = CustomerOrderStatus.CANCELLED;
    }
}
