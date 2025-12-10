package org.supplychain.supplychain.mapper.modelSupplier;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.*;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.supplychain.supplychain.dto.supplyOrder.SupplyOrderDTO;
import org.supplychain.supplychain.dto.supplyOrder.SupplyOrderLineDTO;
import org.supplychain.supplychain.model.SupplyOrder;
import org.supplychain.supplychain.model.SupplyOrderLine;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierOrderMapper {
//    SupplyOrder toEntity(SupplyOrderDTO dto);
//    SupplyOrderDTO toDto(SupplyOrder entity);
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    SupplyOrder updateEntity(@MappingTarget SupplyOrder entity, SupplyOrderDTO dto);

    @Mapping(target = "supplier", ignore = true) // handled in service
    @Mapping(target = "orderLines", ignore = true) // handled in service
    SupplyOrder toEntity(SupplyOrderDTO dto);

    @Mapping(source = "supplier.idSupplier", target = "supplierId")
    @Mapping(target = "orderLines", expression = "java(mapOrderLines(entity.getOrderLines()))")
    SupplyOrderDTO toDto(SupplyOrder entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "supplier", ignore = true) // handled in service
    @Mapping(target = "orderLines", ignore = true) // handled in service
    SupplyOrder updateEntity(@MappingTarget SupplyOrder entity, SupplyOrderDTO dto);

    default List<SupplyOrderLineDTO> mapOrderLines(List<SupplyOrderLine> lines) {
        if (lines == null) return null;
        return lines.stream().map(line -> {
            SupplyOrderLineDTO dto = new SupplyOrderLineDTO();
            dto.setIdLine(line.getIdLine());
            dto.setSupplyOrderId(line.getSupplyOrder().getIdOrder());
            dto.setRawMaterialId(line.getRawMaterial().getIdMaterial());
            dto.setQuantity(line.getQuantity());
            dto.setUnitPrice(line.getUnitPrice());
            return dto;
        }).toList();
    }
}
