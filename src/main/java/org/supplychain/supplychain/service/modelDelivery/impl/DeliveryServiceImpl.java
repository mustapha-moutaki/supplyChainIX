package org.supplychain.supplychain.service.modelDelivery.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.supplychain.supplychain.dto.modelDelivery.DeliveryDto;
import org.supplychain.supplychain.mapper.modelDelivery.DeliveryMapper;
import org.supplychain.supplychain.model.Delivery;
import org.supplychain.supplychain.model.Order;
import org.supplychain.supplychain.enums.OrderStatus;
import org.supplychain.supplychain.repository.approvisionnement.OrderRepository;
import org.supplychain.supplychain.repository.modelDelivery.DeliveryRepository;
import org.supplychain.supplychain.service.modelDelivery.interfaces.IDeliveryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements IDeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderRepository orderRepository;

    @Override
    public DeliveryDto createDelivery(DeliveryDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found for delivery"));

        if (deliveryRepository.existsByOrder(order)) {
            throw new IllegalStateException("This order already has an associated delivery");
        }

        Delivery delivery = deliveryMapper.toEntity(dto);
        delivery.setOrder(order);

        Delivery saved = deliveryRepository.save(delivery);

        return deliveryMapper.toDTO(saved);
    }

    @Override
    public List<DeliveryDto> getAllDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveries.stream()
                .map(deliveryMapper::toDTO)
                .toList();
    }

    @Override
    public DeliveryDto getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found with ID: " + id));
        return deliveryMapper.toDTO(delivery);
    }

    @Override
    public DeliveryDto updateDelivery(Long id, DeliveryDto dto) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found with ID: " + id));

        delivery.setDeliveryAddress(dto.getDeliveryAddress());
        delivery.setDeliveryDate(dto.getDeliveryDate());
        delivery.setDeliveryCost(dto.getDeliveryCost());
        delivery.setDriver(dto.getDriver());
        delivery.setStatus(dto.getStatus());

        Delivery updated = deliveryRepository.save(delivery);

        Order order = updated.getOrder();

        switch (updated.getStatus()) {
            case PLANIFIEE -> order.setStatus(OrderStatus.EN_PREPARATION);
            case EN_COURS -> order.setStatus(OrderStatus.EN_ROUTE);
            case LIVREE -> order.setStatus(OrderStatus.LIVREE);
        }

        orderRepository.save(order);

        return deliveryMapper.toDTO(updated);
    }

    @Override
    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found with ID: " + id));

        deliveryRepository.delete(delivery);
    }
}
