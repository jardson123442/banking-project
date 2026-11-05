package com.v1.bms.project.api.mapper;

import com.v1.bms.project.api.dto.CustomerRequestDTO;
import com.v1.bms.project.domain.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    CustomerEntity toCustomerEntity(CustomerRequestDTO request);
}
