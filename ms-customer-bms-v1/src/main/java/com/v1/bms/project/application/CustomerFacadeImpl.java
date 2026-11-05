package com.v1.bms.project.application;

import com.v1.bms.project.api.assembler.CustomerAssembler;
import com.v1.bms.project.api.disassembler.CustomerDisassembler;
import com.v1.bms.project.api.dto.CustomerRequestDTO;
import com.v1.bms.project.api.dto.CustomerResponseDTO;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.application.port.input.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerFacadeImpl implements CustomerFacade {

    private final CustomerService customerService;
    private final CustomerAssembler customerAssembler;
    private final CustomerDisassembler customerDisassembler;

    @Override
    public CustomerResponseDTO create(CustomerRequestDTO createDto) {
        LogUtils.log(ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "create");

        var customer = customerDisassembler.toEntity(createDto);
        var result = customerService.create(customer);

        return customerAssembler.toModel(result, "Customer created successfully", "Created");
    }

    @Override
    public CustomerResponseDTO findOwner(String customerCpf) {
        LogUtils.log(ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "findOwner");

        var result = customerService.findOwner(customerCpf);
        return customerAssembler.toModel(result, "Customer Account Opening found", "Found");
    }
}
