package com.v1.bms.project.persistence.repository;

import com.v1.bms.project.domain.entity.CustomerAccountOpeningRequestEntity;

import java.util.Optional;

public interface CustomerAccountRepository {

    CustomerAccountOpeningRequestEntity save(CustomerAccountOpeningRequestEntity customerAccountOpeningRequestEntity);

    Optional<CustomerAccountOpeningRequestEntity> findByCpf(String cpf);
}
