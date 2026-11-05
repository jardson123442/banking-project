package com.v1.bms.project.dummies;

import com.v1.bms.project.domain.enums.CustomerStatus;
import com.v1.bms.project.persistence.entity.CustomerPersistenceEntity;

import java.time.LocalDateTime;

public class CustomerCreateDummy {

    public static CustomerPersistenceEntity mockCustomerRequestPersistenceEntity() {
        return CustomerPersistenceEntity.builder()
                .id("eaec1eed-e277-4da6-aa0e-fe98fa10f220")
                .name("Davi")
                .cpf("91209419142")
                .birthDate("24/10/1991")
                .email("davii@gmail.com")
                .status(CustomerStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
