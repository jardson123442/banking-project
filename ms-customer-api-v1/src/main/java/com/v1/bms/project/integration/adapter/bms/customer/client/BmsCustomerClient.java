package com.v1.bms.project.integration.adapter.bms.customer.client;

import com.v1.bms.project.domain.vo.account.opening.CreateCustomerVO;
import com.v1.bms.project.integration.adapter.bms.customer.dto.BmsCustomerResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/bms/v1/customer-account-opening")
public interface BmsCustomerClient {

    @PostExchange
    BmsCustomerResponseDto createCustomer(@RequestBody CreateCustomerVO customerVO);

    @GetExchange("/{customerCpf}")
    BmsCustomerResponseDto getCustomerByCpf(@PathVariable("customerCpf") String customerCpf);
}
