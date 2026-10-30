package com.v1.bms.project.application;

import com.v1.bms.project.api.assembler.CustomerAccountOpeningAssembler;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateResponseDTO;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.service.CustomerAccountOpeningService;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerAccountOpeningFacadeImpl implements CustomerAccountOpeningFacade {

    private final CustomerAccountOpeningService customerAccountOpeningService;
    private final CustomerAccountOpeningAssembler customerAccountOpeningAssembler;

    @Override
    public CustomerAccountOpeningCreateResponseDTO create(CustomerAccountOpeningCreateVO createVO) {
        LogUtils.log(ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "create");

        var result = customerAccountOpeningService.create(createVO);

        return customerAccountOpeningAssembler.toModel(result, "Customer Account Opening created successfully", "Created");
    }

    @Override
    public CustomerAccountOpeningCreateResponseDTO findOwner(String customerCpf) {
        LogUtils.log(ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "findOwner");

        var result = customerAccountOpeningService.findOwner(customerCpf);
        return customerAccountOpeningAssembler.toModel(result, "Customer Account Opening found", "Found");
    }
}
