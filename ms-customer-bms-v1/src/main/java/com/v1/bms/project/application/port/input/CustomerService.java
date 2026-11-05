package com.v1.bms.project.application.port.input;


import com.v1.bms.project.domain.entity.CustomerEntity;
import com.v1.bms.project.domain.vo.account.opening.CustomerResultVO;


public interface CustomerService {

    CustomerResultVO create(CustomerEntity customer);

    CustomerResultVO findOwner(String customerCpf);
}
