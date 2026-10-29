package com.v1.bms.project.application.facade;

import com.v1.bms.project.api.assembler.CustomerAssembler;
import com.v1.bms.project.api.disassembler.CustomerDisassembler;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.integration.adapter.bms.customer.dto.BmsCustomerResponseDto;
import com.v1.bms.project.integration.adapter.bms.customer.dto.CreateCustomerRequestDTO;
import com.v1.bms.project.integration.adapter.bms.customer.service.BmsCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerFacade {

    private final BmsCustomerService bmsCustomerService;
    private final CustomerDisassembler customerDisassembler;

//    public BmsCustomerResponseDto create(CreateCustomerRequestDTO createCustomerRequestDTO) {
//        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "create");
//        LogUtils.log("CreateCustomerRequestDTO {}", createCustomerRequestDTO);
//
//        var createCustomerVO = customerDisassembler.toDomainObject(createCustomerRequestDTO);
//        return bmsCustomerService.createCustomer(createCustomerVO);
//    }

    public void create(CreateCustomerRequestDTO createCustomerRequestDTO) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "create");
        LogUtils.log("CreateCustomerRequestDTO {}", createCustomerRequestDTO);

        var createCustomerVO = customerDisassembler.toDomainObject(createCustomerRequestDTO);
        bmsCustomerService.createCustomer(createCustomerVO);
    }

    public BmsCustomerResponseDto findCustomerByCpf(String cpf) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "getAccount");
        LogUtils.log("getAccountByCpf {}", cpf);

        return bmsCustomerService.getCustomerByCpf(cpf);
    }
}
