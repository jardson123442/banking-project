package com.v1.bms.project.api.disassembler;

import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerAccountOpeningMapper {

    CustomerAccountOpeningCreateVO toCustomerAccountCreateVO(CustomerAccountOpeningCreateRequestDTO customerAccountOpeningCreateRequestDTO);
}
