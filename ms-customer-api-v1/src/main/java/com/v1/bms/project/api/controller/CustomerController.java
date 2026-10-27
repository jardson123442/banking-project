package com.v1.bms.project.api.controller;

import com.v1.bms.project.api.openapi.controller.CustomerAccountOpeningControllerOpenApi;

import com.v1.bms.project.application.facade.CustomerFacade;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.integration.adapter.bms.customer.dto.BmsCustomerResponseDto;
import com.v1.bms.project.integration.adapter.bms.customer.dto.CreateCustomerRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.v1.bms.project.api.UrlConstant.CUSTOMER_ACCOUNT_URL_2;
import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(CUSTOMER_ACCOUNT_URL_2)
public class CustomerController implements CustomerAccountOpeningControllerOpenApi {

    private final CustomerFacade customerFacade;

    @Override
    public BmsCustomerResponseDto create(CreateCustomerRequestDTO customerRequestDTO) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "create");
        LogUtils.log("CreateCustomerRequestDTO {}", customerRequestDTO);

        return this.customerFacade.create(customerRequestDTO);
    }

    @Override
    public BmsCustomerResponseDto findOwner(String customerCpf) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "findOwner");
        LogUtils.log("Customer CPF {}", customerCpf);

        return this.customerFacade.findCustomerByCpf(customerCpf);
    }
}
