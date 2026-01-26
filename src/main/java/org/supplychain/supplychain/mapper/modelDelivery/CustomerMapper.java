package org.supplychain.supplychain.mapper.modelDelivery;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.supplychain.supplychain.dto.modelDelivery.CustomerDto;
import org.supplychain.supplychain.model.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    //CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "idCustomer", target = "id")
    CustomerDto toDto(Customer customer);

    @Mapping(source = "id", target = "idCustomer")
    Customer toEntity(CustomerDto customerDto);
}

