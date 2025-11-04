package org.supplychain.supplychain.service.modelSupplier.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.supplychain.supplychain.dto.order.OrderDTO;
import org.supplychain.supplychain.mapper.modelSupplier.OrderMapper;
import org.supplychain.supplychain.model.Order;
import org.supplychain.supplychain.repository.approvisionnement.OrderRepository;
import org.supplychain.supplychain.repository.modelDelivery.CustomerRepository;
import org.supplychain.supplychain.service.modelSupplier.OrderServiec;


import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderServiec {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
//    private final ProductOrderRepository productOrderRepository;

    //    private final ProductOrderRepository productOrderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDTO createOrder(OrderDTO dto) {

        if (dto.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer must not be null");
        }

        Order order = orderMapper.toEntity(dto);

        // relate customer
        order.setCustomer(customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Client introuvable")));

        // relate ProductOrders
//       order.setProductOrders(productOrderRepository.findAllById(dto.getProductOrderIds()));

        Order saved = orderRepository.save(order);

        return orderMapper.toDto(saved);
    }

    @Override
    public OrderDTO updateOrder(Long id, OrderDTO dto) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order introuvable"));

        existing.setStatus(dto.getStatus());
        existing.setTotalAmount(dto.getTotalAmount());


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
