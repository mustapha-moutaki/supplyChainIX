package org.supplychain.supplychain.service.modelDelivery.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.supplychain.supplychain.dto.modelDelivery.DeliveryDto;
import org.supplychain.supplychain.mapper.modelDelivery.DeliveryMapper;
import org.supplychain.supplychain.model.Delivery;
import org.supplychain.supplychain.model.Order;
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
            throw new IllegalStateException("Cette commande a déjà une livraison associée");
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
                .orElseThrow(() -> new IllegalArgumentException("Livraison introuvable avec l’ID : " + id));
        return deliveryMapper.toDTO(delivery);
    }

    @Override
    public DeliveryDto updateDelivery(Long id, DeliveryDto dto) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livraison introuvable avec l’ID : " + id));

        // Mise à jour des champs autorisés
        delivery.setDeliveryAddress(dto.getDeliveryAddress());
        delivery.setDeliveryDate(dto.getDeliveryDate());
        delivery.setDeliveryCost(dto.getDeliveryCost());
        delivery.setDriver(dto.getDriver());
        delivery.setStatus(dto.getStatus());

        Delivery updated = deliveryRepository.save(delivery);
        return deliveryMapper.toDTO(updated);
    }


    @Override
    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livraison introuvable avec l’ID : " + id));

        deliveryRepository.delete(delivery);
    }

}
