package com.v1.bms.project.persistence.repository.jpa;

import com.v1.bms.project.application.port.output.CustomerCpfAlreadyInUseException;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.application.port.output.CustomerRepository;
import com.v1.bms.project.domain.entity.CustomerEntity;
import com.v1.bms.project.persistence.mapper.CustomerEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLException;
import java.util.Optional;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

@Repository("CustomerCustomJpaRepository")
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;
    private final CustomerEntityMapper entityMapper;

    @Override
    public CustomerEntity save(CustomerEntity customerEntity) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "save");

        try {
            var entity = entityMapper.toPersonalAccountOpeningRequestPersistenceEntity(customerEntity);
            var saveEntity = jpaRepository.save(entity);

            return entityMapper.toPersonalAccountOpeningRequestEntity(saveEntity);
        } catch (DataIntegrityViolationException exception) {
            if (isCpfConstraintViolation(exception)) {
                LogUtils.warn("CPF uniqueness constraint rejected customer persistence");
                throw new CustomerCpfAlreadyInUseException(
                        "CPF uniqueness constraint violated for customer: " + customerEntity.getCpf()
                );
            }
            throw exception;
        }
    }

    @Override
    public Optional<CustomerEntity> findByCpf(String cpf) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "found");

        return jpaRepository.findByCpf(cpf).map(entityMapper::toPersonalAccountOpeningRequestEntity);
    }

    private boolean isCpfConstraintViolation(Throwable exception) {
        for (var cause = exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof ConstraintViolationException constraintViolation) {
                var constraintName = constraintViolation.getConstraintName();
                if (constraintName != null && constraintName.toLowerCase().contains("cpf")) {
                    return true;
                }
            }

            if (cause instanceof SQLException sqlException
                    && "23505".equals(sqlException.getSQLState())
                    && sqlException.getMessage() != null
                    && sqlException.getMessage().toLowerCase().contains("cpf")) {
                return true;
            }
        }
        return false;
    }
}
