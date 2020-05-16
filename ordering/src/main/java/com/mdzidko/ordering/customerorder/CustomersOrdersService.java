package com.mdzidko.ordering.customerorder;

import com.mdzidko.ordering.customer.Customer;
import com.mdzidko.ordering.customer.CustomerNotFoundException;
import com.mdzidko.ordering.product.Product;
import com.mdzidko.ordering.product.ProductNotFoundException;
import com.mdzidko.ordering.customer.CustomersRepository;
import com.mdzidko.ordering.product.ProductsRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


public class CustomersOrdersService {
    private final CustomersOrdersRepository customersOrdersRepository;
    private final CustomersRepository customersRepository;
    private final ProductsRepository productsRepository;

    public CustomersOrdersService(final CustomersOrdersRepository customersOrdersRepository,
                                  final CustomersRepository customersRepository, final ProductsRepository productsRepository) {
        this.customersOrdersRepository = customersOrdersRepository;
        this.customersRepository = customersRepository;
        this.productsRepository = productsRepository;
    }

    public CustomerOrder createCustomerOrder(final UUID customerId){
        Customer customer = findCustomerById(customerId);
        return customersOrdersRepository.save(CustomerOrder.create(customer));
    }

    public Iterable<CustomerOrder> findAllOrders(){
        return customersOrdersRepository.findAll();
    }

    public Iterable<CustomerOrder> findAllCustomerOrders(final UUID customerId){
        Customer customer = findCustomerById(customerId);
        return customersOrdersRepository.findAllByCustomer(customer);
    }

    public CustomerOrder findOrderById(final UUID orderId){
        return customersOrdersRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Transactional
    public CustomerOrder addProductToOrder(final UUID orderId, final UUID productId, final int productQuantity){
        CustomerOrder customerOrder = findOrderById(orderId);

        if(customerOrder.getStatus() != CustomerOrderStatus.NEW){
            throw new BadOrderStatusException(customerOrder.getStatus());
        }

        Product product = findProductById(productId);
        product.removeFromStock(productQuantity);

        customerOrder.getCustomer().reserveCredits(productQuantity * product.getPrice());

        customerOrder.addNewLine(product, productQuantity);

        return customerOrder;
    }

    @Transactional
    public CustomerOrder cancelOrder(final UUID orderId){
        CustomerOrder customerOrder = findOrderById(orderId);

        if(customerOrder.getStatus() == CustomerOrderStatus.CANCELLED){
            throw new BadOrderStatusException(CustomerOrderStatus.CANCELLED);
        }

        addQuantityForAllOrderLinesProducts(customerOrder);

        double orderPrice = customerOrder.calculateOrderValue();
        customerOrder.getCustomer().addCredits(orderPrice);

        customerOrder.cancel();

        return customerOrder;
    }

    public Iterable<CustomerOrderLine> findAllCustomerOrderLines(final UUID orderId) {
        return findOrderById(orderId).getLines();
    }

    private void addQuantityForAllOrderLinesProducts(final CustomerOrder customerOrder){
        customerOrder
                .getLines()
                .forEach(orderLine -> {
                    int productQuantity = orderLine.getQuantity();
                    orderLine.getProduct().addToStock(productQuantity);
                });
    }

    private Customer findCustomerById(final UUID customerId){
        return customersRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    private Product findProductById(final UUID productId){
        return productsRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
