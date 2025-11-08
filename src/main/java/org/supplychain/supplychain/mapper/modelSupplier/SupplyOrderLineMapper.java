package org.supplychain.supplychain.mapper.modelSupplier;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.supplychain.supplychain.dto.supplyOrder.SupplyOrderLineDTO;
import org.supplychain.supplychain.model.SupplyOrderLine;

@Mapper(componentModel = "spring")
public interface SupplyOrderLineMapper {

    SupplyOrderLineMapper INSTANCE = Mappers.getMapper(SupplyOrderLineMapper.class);

    @Mapping(source = "supplyOrder.idOrder", target = "supplyOrderId")
    @Mapping(source = "rawMaterial.idMaterial", target = "rawMaterialId")
    SupplyOrderLineDTO toDto(SupplyOrderLine entity);

    @Mapping(source = "supplyOrderId", target = "supplyOrder.idOrder")
    @Mapping(source = "rawMaterialId", target = "rawMaterial.idMaterial")
    SupplyOrderLine toEntity(SupplyOrderLineDTO dto);
}
