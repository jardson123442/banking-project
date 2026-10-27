package com.v1.bms.project.api.disassembler;

import com.v1.bms.project.application.mapper.CustomerMapper;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.vo.account.opening.CreateCustomerVO;
import com.v1.bms.project.integration.adapter.bms.customer.dto.CreateCustomerRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerDisassembler {

    public CreateCustomerVO toDomainObject(CreateCustomerRequestDTO customerAccountOpeningCreateRequestDTO) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "toDomainObject");

        var createCustomerVo = CustomerMapper.INSTANCE.toCustomerAccountOpeningCreateVO(customerAccountOpeningCreateRequestDTO);
        LogUtils.log("Created CreateCustomerVO: {}", createCustomerVo);
        return createCustomerVo;
    }
}
