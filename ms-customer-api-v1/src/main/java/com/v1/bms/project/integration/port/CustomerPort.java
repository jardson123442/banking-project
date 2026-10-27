package com.v1.bms.project.integration.port;

import com.v1.bms.project.domain.vo.account.opening.CreateCustomerVO;
import com.v1.bms.project.integration.adapter.bms.customer.dto.BmsCustomerResponseDto;

public interface CustomerPort {

    BmsCustomerResponseDto createCustomer(CreateCustomerVO createCustomerVO);

    BmsCustomerResponseDto getCustomerByCpf(String cpf);
}
