package com.v1.bms.project.application.mapper;

import com.v1.bms.project.domain.vo.account.opening.CreateCustomerVO;
import com.v1.bms.project.integration.adapter.bms.customer.dto.CreateCustomerRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(CustomerMapper.class);

    CreateCustomerVO toCustomerAccountOpeningCreateVO(CreateCustomerRequestDTO customerAccountOpeningCreateRequestDTO);
}
