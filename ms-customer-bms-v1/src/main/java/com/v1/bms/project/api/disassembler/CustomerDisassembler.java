package com.v1.bms.project.api.disassembler;

import com.v1.bms.project.application.mapper.CustomerAccountOpeningMapper;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerDisassembler {

    public CustomerAccountOpeningCreateVO toDomainObject(CustomerAccountOpeningCreateRequestDTO customerAccountOpeningCreateRequestDTO, String message, String status) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "toDomainObject");

        var createCustomerVo = CustomerAccountOpeningMapper.INSTANCE.toCustomerAccountCreateVO(customerAccountOpeningCreateRequestDTO);
        LogUtils.log("Created CustomerAccountOpeningCreateVO: {}", createCustomerVo);
        return createCustomerVo;
    }
}
