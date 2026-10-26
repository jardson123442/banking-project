package com.v1.bms.project.api.assembler;

import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateResponseDTO;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Component
@RequiredArgsConstructor
public class CustomerAccountOpeningAssembler {

    public CustomerAccountOpeningCreateResponseDTO toModel(CustomerAccountOpeningResultVO result, String message, String status) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "toModel");

        return CustomerAccountOpeningCreateResponseDTO.builder()
                .data(result)
                .message(message)
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
