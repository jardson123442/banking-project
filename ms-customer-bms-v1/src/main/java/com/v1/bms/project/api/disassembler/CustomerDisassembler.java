package com.v1.bms.project.api.disassembler;

import com.v1.bms.project.api.dto.CustomerRequestDTO;
import com.v1.bms.project.api.mapper.CustomerMapper;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Component
@RequiredArgsConstructor
public class CustomerDisassembler {

    private final CustomerMapper accountOpeningMapper;

    public CustomerEntity toEntity(
            CustomerRequestDTO request) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "toEntity");

        return accountOpeningMapper.toCustomerEntity(request);
    }
}
