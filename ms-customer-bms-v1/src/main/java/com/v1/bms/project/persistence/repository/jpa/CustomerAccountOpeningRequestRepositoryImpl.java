package com.v1.bms.project.persistence.repository.jpa;

import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.entity.CustomerAccountOpeningRequestEntity;
import com.v1.bms.project.persistence.mapper.CustomerOpeningEntityMapper;
import com.v1.bms.project.persistence.repository.CustomerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Repository
@RequiredArgsConstructor
public class CustomerAccountOpeningRequestRepositoryImpl  implements CustomerAccountRepository {

    private final CustomerAccountJpaRepository jpaRepository;
    private final CustomerOpeningEntityMapper entityMapper;

    @Override
    public CustomerAccountOpeningRequestEntity save(CustomerAccountOpeningRequestEntity customerAccountOpeningRequestEntity) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "save");

        var entity = entityMapper.toPersonalAccountOpeningRequestPersistenceEntity(customerAccountOpeningRequestEntity);
        var saveEntity = jpaRepository.save(entity);

        return entityMapper.toPersonalAccountOpeningRequestEntity(saveEntity);
    }

    @Override
    public Optional<CustomerAccountOpeningRequestEntity> findByCpf(String cpf) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "found");

        return jpaRepository.findByCpf(cpf).map(entityMapper::toPersonalAccountOpeningRequestEntity);
    }
}
