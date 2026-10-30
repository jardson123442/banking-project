package com.v1.bms.project.domain.service;


import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningResultVO;


public interface CustomerAccountOpeningService {

    CustomerAccountOpeningResultVO create(CustomerAccountOpeningCreateVO createVO);

    CustomerAccountOpeningResultVO findOwner(String customerCpf);
}
