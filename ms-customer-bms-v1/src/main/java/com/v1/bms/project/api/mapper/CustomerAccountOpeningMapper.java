package com.v1.bms.project.api.mapper;

import com.v1.bms.project.api.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerAccountOpeningMapper {

    CustomerAccountOpeningCreateVO toCustomerAccountCreateVO(CustomerAccountOpeningCreateRequestDTO request);
}
