package com.mdzidko.ordering.customersorders.customerorder.domain;


import com.mdzidko.ordering.customersorders.customerorder.domain.event.CustomerOrderCanceledEvent;
import com.mdzidko.ordering.customersorders.customerorder.domain.dto.CustomerOrderDto;
import com.mdzidko.ordering.customersorders.customerorder.domain.event.CustomerOrderEvent;
import com.mdzidko.ordering.customersorders.customerorder.domain.event.ProductAddedToCustomerOrderEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
class CustomerOrder {
    @Id
    private UUID id;
    private UUID customerId;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CustomerOrderLine> lines = new ArrayList<>();
    private CustomerOrderStatus status = CustomerOrderStatus.NEW;

    @Transient
    private Collection<CustomerOrderEvent> domainEvents = new ArrayList<>();

    private CustomerOrder(final UUID customerId) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
    }

    static CustomerOrder create(final UUID customerId){
        return new CustomerOrder(customerId);
    }

    double calculateOrderValue() {
        return lines
                .stream()
                .map(CustomerOrderLine::calculateTotalValue)
                .reduce(0.0, (sum, lineValue) -> sum + lineValue);
    }

    CustomerOrder addNewLine(final UUID productId, final int productQuantity, final double productPrice) {
        lines
                .stream()
                .filter(orderLine -> orderLine.getProductId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        line -> line.addQuantity(productQuantity),
                        () -> lines.add(CustomerOrderLine.create(productId, productQuantity, productPrice)));

        domainEvents.add(
                new ProductAddedToCustomerOrderEvent(
                    this.id,
                    this.customerId,
                    this.calculateOrderValue(),
                    LocalDateTime.now(),
                    productId,
                    productPrice,
                    productQuantity)
        );

        return this;
    }

    CustomerOrder removeProduct(final UUID productId, final int productQuantity) {
        lines
                .stream()
                .filter(orderLine -> orderLine.getProductId().equals(productId))
                .findFirst()
                .ifPresent(
                        line -> {
                            if(line.getQuantity() == productQuantity){
                                lines.remove(line);
                            }
                            else{
                                line.removeQuantity(productQuantity);
                            }
                        }
                );

        return this;
    }

    CustomerOrder cancel() {
        this.status = CustomerOrderStatus.CANCELLED;

        domainEvents.add(
                new CustomerOrderCanceledEvent(
                        this.id,
                        this.customerId,
                        this.calculateOrderValue(),
                        LocalDateTime.now(),
                        lines.stream()
                                .map(CustomerOrderLine::dto)
                                .collect(Collectors.toList())
                )
        );

        return this;
    }

    CustomerOrder setAsNew() {
        this.status = CustomerOrderStatus.NEW;
        return this;
    }

    CustomerOrderDto dto(){
        return CustomerOrderDto
                .builder()
                .id(this.id)
                .customerId(this.customerId)
                .status(this.status.toString())
                .value(calculateOrderValue())
                .build();
    }

    @DomainEvents
    Collection<CustomerOrderEvent> domainEvents(){
        return this.domainEvents;
    }

    @AfterDomainEventPublication
    void afterDomainEventsPublication(){
        this.domainEvents.clear();
    }
}
