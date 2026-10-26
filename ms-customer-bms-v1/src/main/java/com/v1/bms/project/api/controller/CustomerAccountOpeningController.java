package com.v1.bms.project.api.controller;

import com.v1.bms.project.api.UrlConstant;
import com.v1.bms.project.api.openapi.controller.CustomerAccountOpeningControllerOpenApi;
import com.v1.bms.project.application.CustomerAccountOpeningFacade;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateResponseDTO;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(UrlConstant.CUSTOMER_ACCOUNT_URL)
public class CustomerAccountOpeningController implements CustomerAccountOpeningControllerOpenApi {
    private final CustomerAccountOpeningFacade customerAccountOpeningFacade;

    @Override
    public CustomerAccountOpeningCreateResponseDTO create(CustomerAccountOpeningCreateRequestDTO customerAccountOpeningCreateRequestDTO) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "create");

        return customerAccountOpeningFacade.create(customerAccountOpeningCreateRequestDTO);
    }

    @Override
    public CustomerAccountOpeningCreateResponseDTO findOwner(String customerCpf) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "findOwned");

        return customerAccountOpeningFacade.findOwner(customerCpf);
    }
}
