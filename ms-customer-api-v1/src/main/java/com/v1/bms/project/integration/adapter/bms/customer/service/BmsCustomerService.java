package com.v1.bms.project.integration.adapter.bms.customer.service;

import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.vo.account.opening.CreateCustomerVO;
import com.v1.bms.project.integration.adapter.bms.customer.client.BmsCustomerClient;
import com.v1.bms.project.integration.adapter.bms.customer.dto.BmsCustomerResponseDto;
import com.v1.bms.project.integration.port.CustomerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Slf4j
@Service
@RequiredArgsConstructor
public class BmsCustomerService implements CustomerPort {

    private final BmsCustomerClient bmsCustomerClient;


    @Override
    public BmsCustomerResponseDto createCustomer(CreateCustomerVO createCustomerVO) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "createCustomer");
        LogUtils.log("CreateCustomerVO {}", createCustomerVO);

        return this.bmsCustomerClient.createCustomer(createCustomerVO);
    }

    @Override
    public BmsCustomerResponseDto getCustomerByCpf(String cpf) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "getCustomerByCpf");
        LogUtils.log("CPF: {}", cpf);

        return this.bmsCustomerClient.getCustomerByCpf(cpf);
    }
}

