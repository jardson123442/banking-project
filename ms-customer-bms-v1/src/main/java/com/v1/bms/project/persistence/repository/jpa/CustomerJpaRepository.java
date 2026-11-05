package com.v1.bms.project.persistence.repository.jpa;

import com.v1.bms.project.persistence.entity.CustomerPersistenceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerPersistenceEntity, String> {

    CustomerPersistenceEntity save(@NonNull CustomerPersistenceEntity customerRequestPersistence);

    Optional<CustomerPersistenceEntity> findByCpf(String cpf);
}
