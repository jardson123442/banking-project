package com.v1.bms.project.application;

import com.v1.bms.project.api.assembler.CustomerAccountOpeningAssembler;
import com.v1.bms.project.api.disassembler.CustomerDisassembler;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateResponseDTO;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.service.CustomerAccountOpeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerAccountOpeningFacadeImpl implements CustomerAccountOpeningFacade {

    private final CustomerAccountOpeningService customerAccountOpeningService;
    private final CustomerAccountOpeningAssembler customerAccountOpeningAssembler;
    private final CustomerDisassembler customerDisassembler;


    @Override
    public CustomerAccountOpeningCreateResponseDTO create(CustomerAccountOpeningCreateRequestDTO customerAccountOpeningCreateRequestDTO) {
        LogUtils.log(ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "create");

        var customerAccountOpeningCreateVO = customerDisassembler.toDomainObject(customerAccountOpeningCreateRequestDTO, "Customer Account Opening created successfully", "Created");
        var result = customerAccountOpeningService.create(customerAccountOpeningCreateVO);

        return customerAccountOpeningAssembler.toModel(result, "Customer Account Opening created successfully", "Created");
    }

    @Override
    public CustomerAccountOpeningCreateResponseDTO findOwner(String customerCpf) {
        LogUtils.log(ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "findOwner");

        var result = customerAccountOpeningService.findOwner(customerCpf);
        return customerAccountOpeningAssembler.toModel(result, "Customer Account Opening found", "Found");
    }
}
