package org.supplychain.supplychain.mapper.modelSupplier;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.supplychain.supplychain.dto.supplyOrder.SupplyOrderDTO;
import org.supplychain.supplychain.model.SupplyOrder;

@Mapper(componentModel = "spring")
public interface SupplierOrderMapper {
    SupplyOrder toEntity(SupplyOrderDTO dto);
    SupplyOrderDTO toDto(SupplyOrder entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SupplyOrder updateEntity(@MappingTarget SupplyOrder entity, SupplyOrderDTO dto);
}
