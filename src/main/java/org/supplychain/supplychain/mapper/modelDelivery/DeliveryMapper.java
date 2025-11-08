package org.supplychain.supplychain.mapper.modelDelivery;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.supplychain.supplychain.dto.modelDelivery.DeliveryDto;
import org.supplychain.supplychain.model.Delivery;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    // Entity → DTO
    @Mapping(target = "orderId", source = "order.idOrder")
    DeliveryDto toDTO(Delivery delivery);

    // DTO → Entity
    @Mapping(target = "order", ignore = true)
    Delivery toEntity(DeliveryDto dto);
}
