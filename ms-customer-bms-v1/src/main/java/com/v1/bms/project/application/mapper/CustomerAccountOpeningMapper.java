package com.v1.bms.project.application.mapper;

import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerAccountOpeningMapper {
    CustomerAccountOpeningMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(CustomerAccountOpeningMapper.class);

    CustomerAccountOpeningCreateVO toCustomerAccountCreateVO(CustomerAccountOpeningCreateRequestDTO customerAccountOpeningCreateRequestDTO);
}
