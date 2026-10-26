package com.v1.bms.project.persistence.repository;

import com.v1.bms.project.persistence.entity.CustomerAccountOpeningRequestPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerAccountOpeningRequestRepository extends JpaRepository<CustomerAccountOpeningRequestPersistenceEntity, String> {

    CustomerAccountOpeningRequestPersistenceEntity save(CustomerAccountOpeningRequestPersistenceEntity customerAccountOpeningRequestEntity);

    Optional<CustomerAccountOpeningRequestPersistenceEntity> findByCpf(String cpf);
}
