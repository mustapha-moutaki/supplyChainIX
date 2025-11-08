package org.supplychain.supplychain.service.modelDelivery.interfaces;

import org.supplychain.supplychain.dto.modelDelivery.DeliveryDto;

import java.util.List;

public interface IDeliveryService {


    DeliveryDto createDelivery(DeliveryDto dto);

    List<DeliveryDto> getAllDeliveries();

    DeliveryDto getDeliveryById(Long id);

    DeliveryDto updateDelivery(Long id, DeliveryDto dto);

    void deleteDelivery(Long id);


}
