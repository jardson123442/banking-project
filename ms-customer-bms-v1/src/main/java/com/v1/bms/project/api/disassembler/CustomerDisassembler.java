package com.v1.bms.project.api.disassembler;

import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Component
@RequiredArgsConstructor
public class CustomerDisassembler {

    private final CustomerAccountOpeningMapper accountOpeningMapper;

    public CustomerAccountOpeningCreateVO toDomainObject(
            CustomerAccountOpeningCreateRequestDTO request) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "toDomainObject");
        return accountOpeningMapper.toCustomerAccountCreateVO(request);
    }
}
