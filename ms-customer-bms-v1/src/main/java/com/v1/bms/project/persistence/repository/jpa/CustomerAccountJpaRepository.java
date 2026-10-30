package com.v1.bms.project.persistence.repository.jpa;

import com.v1.bms.project.persistence.entity.CustomerAccountOpeningRequestPersistenceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerAccountJpaRepository extends JpaRepository<CustomerAccountOpeningRequestPersistenceEntity, String> {

    CustomerAccountOpeningRequestPersistenceEntity save(@NonNull CustomerAccountOpeningRequestPersistenceEntity customerRequestPersistence);

    Optional<CustomerAccountOpeningRequestPersistenceEntity> findByCpf(String cpf);
}
