package org.supplychain.supplychain.service.modelSupplier.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.supplychain.supplychain.dto.order.OrderDTO;
import org.supplychain.supplychain.dto.order.ProductOrderDTO;
import org.supplychain.supplychain.enums.OrderStatus;
import org.supplychain.supplychain.mapper.Production.ProductOrderMapper;
import org.supplychain.supplychain.mapper.modelSupplier.OrderMapper;
import org.supplychain.supplychain.model.Customer;
import org.supplychain.supplychain.model.Order;
import org.supplychain.supplychain.model.Product;
import org.supplychain.supplychain.model.ProductOrder;
import org.supplychain.supplychain.repository.Production.ProductRepository;
import org.supplychain.supplychain.repository.approvisionnement.OrderRepository;
import org.supplychain.supplychain.repository.modelDelivery.CustomerRepository;
import org.supplychain.supplychain.service.modelSupplier.OrderServiec;


import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderServiec {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductOrderMapper productOrderMapper;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;

//    @Override
//    public OrderDTO createOrder(OrderDTO dto) {
//
//        Order order = orderMapper.toEntity(dto);
//
//        // relate customer
//        order.setCustomer(customerRepository.findById(dto.getCustomerId())
//                .orElseThrow(() -> new RuntimeException("Client introuvable")));
//
//        // relate ProductOrders
////       order.setProductOrders(productOrderRepository.findAllById(dto.getProductOrderIds()));
//
//        Order saved = orderRepository.save(order);
//
//        return orderMapper.toDto(saved);
//    }
//public OrderDTO createOrder(OrderDTO dto) {
//
//
//    Customer customer = customerRepository.findById(dto.getCustomerId())
//            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
//
//    Order order = orderMapper.toEntity(dto);
//    order.setCustomer(customer);
//    order.setStatus(dto.getStatus() != null ? dto.getStatus() : OrderStatus.EN_PREPARATION);
//    final Order finalOrder = order;
//    List<ProductOrder> productOrders = dto.getProductOrders().stream().map(poDTO -> {
//        Product product = productRepository.findById(poDTO.getProductId())
//                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + poDTO.getProductId()));
//
//
//        if (product.getStock() < poDTO.getQuantity()) {
//            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
//        }
//
//
//        ProductOrder productOrder = productOrderMapper.toEntity(poDTO);
//        productOrder.setOrder(finalOrder);
//        productOrder.setProduct(product);
//        productOrder.setUnitPrice(product.getCost());
//        productOrder.setTotalPrice(product.getCost().multiply(BigDecimal.valueOf(poDTO.getQuantity())));
//
//
//        product.setStock(product.getStock() - poDTO.getQuantity());
//
//        return productOrder;
//    }).toList();
//
//
//    order.setProductOrders(productOrders);
//
//
//    order = orderRepository.save(order);
//
//    return orderMapper.toDto(order);
//}

@Override
public OrderDTO createOrder(OrderDTO dto) {
    // ---------------------------
    // 1. Fetch customer from DB
    // ---------------------------
    Customer customer = customerRepository.findById(dto.getCustomerId())
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

    // ---------------------------
    // 2. Create new Order entity from DTO
    // ---------------------------
    Order order = orderMapper.toEntity(dto);
    order.setCustomer(customer);

    // Set default status if not provided
    order.setStatus(dto.getStatus() != null ? dto.getStatus() : OrderStatus.EN_PREPARATION);

    final Order finalOrder = order; // for lambda reference in streams

    // ---------------------------
    // 3. Map ProductOrders from DTO
    //    and check stock
    // ---------------------------
    List<ProductOrder> productOrders = dto.getProductOrders().stream().map(poDTO -> {
        // Fetch product from DB
        Product product = productRepository.findById(poDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + poDTO.getProductId()));

        // ---------------------------
        // 3a. Check available stock
        // ---------------------------
        // Example:
        // product.getStock() = 10
        // poDTO.getQuantity() = 5
        if (poDTO.getQuantity() > product.getStock()) {
            // Stock not enough → stop this order and return warning
            throw new IllegalArgumentException(
                    "Not enough stock for product: " + product.getName() +
                            ". Available: " + product.getStock() + ", requested: " + poDTO.getQuantity()
            );
        }

        // ---------------------------
        // 3b. Create ProductOrder entity
        // ---------------------------
        ProductOrder productOrder = productOrderMapper.toEntity(poDTO);
        productOrder.setOrder(finalOrder); // link to parent Order
        productOrder.setProduct(product);
        productOrder.setUnitPrice(product.getCost()); // price per unit
        productOrder.setTotalPrice(product.getCost().multiply(BigDecimal.valueOf(poDTO.getQuantity())));

        // ---------------------------
        // 3c. Deduct stock for product
        // ---------------------------
        product.setStock(product.getStock() - poDTO.getQuantity());
        productRepository.save(product); // update product stock in DB

        return productOrder;
    }).toList();

    // ---------------------------
    // 4. Attach productOrders to Order
    // ---------------------------
    order.setProductOrders(productOrders);

    // ---------------------------
    // 5. Calculate totalAmount before saving
    // ---------------------------
    BigDecimal totalAmount = productOrders.stream()
            .map(ProductOrder::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    order.setTotalAmount(totalAmount); // assign totalAmount

    // ---------------------------
    // 6. Save order to DB
    // ---------------------------
    order = orderRepository.save(order);

    // ---------------------------
    // 7. Return OrderDTO
    // ---------------------------
    return orderMapper.toDto(order);
}

    @Override
    public OrderDTO updateOrder(Long id, OrderDTO dto) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order introuvable"));

        existing.setStatus(dto.getStatus());
//        existing.setTotalAmount(dto.getTotalAmount());


        Order saved = orderRepository.save(existing);
        return orderMapper.toDto(saved);
    }

    @Override
    public void deleteOrder(Long id) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order introuvable"));

        // we wil add condition if order has delivery we con't not remove it
        if (existing.getDelivery() != null) {
            throw new RuntimeException("Impossible de supprimer : livraison existante");
        }

        orderRepository.delete(existing);
    }

    @Override
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);
    }


    @Override
    public OrderDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Order introuvable"));
    }

}
