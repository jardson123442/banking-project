package com.v1.bms.project.application.port.output;

import com.v1.bms.project.domain.entity.CustomerEntity;

import java.util.Optional;

public interface CustomerRepository {

    CustomerEntity save(CustomerEntity customerEntity);

    Optional<CustomerEntity> findByCpf(String cpf);
}
