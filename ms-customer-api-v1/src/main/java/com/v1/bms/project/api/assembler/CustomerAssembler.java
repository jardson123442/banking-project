package com.v1.bms.project.api.assembler;

import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.vo.account.opening.ResultCustomerVO;
import com.v1.bms.project.integration.adapter.bms.customer.dto.BmsCustomerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Component
@RequiredArgsConstructor
public class CustomerAssembler {

    public BmsCustomerResponseDto toModel(ResultCustomerVO result, String message, String status) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "toModel");

        return BmsCustomerResponseDto.builder()
                .data(result)
                .message(message)
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
