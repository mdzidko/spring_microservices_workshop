package com.mdzidko.ordering.customersorders.customerorder.domain;


import com.mdzidko.ordering.customersorders.customerorder.domain.dto.CustomerOrderDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}
