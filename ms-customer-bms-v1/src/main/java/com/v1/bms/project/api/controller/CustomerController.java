package com.v1.bms.project.api.controller;

import com.v1.bms.project.api.UrlConstant;
import com.v1.bms.project.api.openapi.controller.CustomerControllerOpenApi;
import com.v1.bms.project.application.CustomerFacade;
import com.v1.bms.project.api.dto.CustomerRequestDTO;
import com.v1.bms.project.api.dto.CustomerResponseDTO;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlConstant.CUSTOMER_ACCOUNT_URL)
public class CustomerController implements CustomerControllerOpenApi {
    private final CustomerFacade customerAccountOpeningFacade;

    @Override
    public CustomerResponseDTO create(CustomerRequestDTO customerRequestDTO) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "create");

        return customerAccountOpeningFacade.create(customerRequestDTO);
    }

    @Override
    public CustomerResponseDTO findOwner(String customerCpf) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "findOwned");

        return customerAccountOpeningFacade.findOwner(customerCpf);
    }
}
