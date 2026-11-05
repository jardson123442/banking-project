package com.v1.bms.project.persistence.repository.jpa;

import com.v1.bms.project.application.port.output.CustomerCpfAlreadyInUseException;
import com.v1.bms.project.domain.entity.CustomerEntity;
import com.v1.bms.project.domain.enums.CustomerStatus;
import com.v1.bms.project.persistence.entity.CustomerPersistenceEntity;
import com.v1.bms.project.persistence.mapper.CustomerEntityMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryImplTest {

    private static final String CPF = "12345678901";

    @Mock
    private CustomerJpaRepository jpaRepository;

    @Mock
    private CustomerEntityMapper entityMapper;

    private CustomerRepositoryImpl customerRepository;
    private CustomerEntity customer;
    private CustomerPersistenceEntity persistenceEntity;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepositoryImpl(jpaRepository, entityMapper);
        customer = CustomerEntity.builder()
                .name("John Doe")
                .cpf(CPF)
                .birthDate("01/01/1990")
                .email("john.doe@example.com")
                .status(CustomerStatus.ACTIVE)
                .build();
        persistenceEntity = CustomerPersistenceEntity.builder()
                .name("John Doe")
                .cpf(CPF)
                .birthDate("01/01/1990")
                .email("john.doe@example.com")
                .status(CustomerStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Deve traduzir violação da constraint de CPF para a exceção da porta")
    void shouldTranslateCpfConstraintViolationToPortException() {
        when(entityMapper.toPersonalAccountOpeningRequestPersistenceEntity(customer))
                .thenReturn(persistenceEntity);
        when(jpaRepository.save(persistenceEntity))
                .thenThrow(new DataIntegrityViolationException(
                        "Unique constraint violation",
                        new ConstraintViolationException(
                                "Unique constraint violation",
                                new SQLException("duplicate key", "23505"),
                                "uk_customer_account_opening_cpf"
                        )
                ));

        var exception = assertThrows(
                CustomerCpfAlreadyInUseException.class,
                () -> customerRepository.save(customer)
        );

        assertEquals(
                "CPF uniqueness constraint violated for customer: " + CPF,
                exception.getMessage()
        );
        verify(jpaRepository).save(persistenceEntity);
    }

    @Test
    @DisplayName("Deve propagar violações de integridade que não sejam do CPF")
    void shouldPropagateOtherDataIntegrityViolations() {
        when(entityMapper.toPersonalAccountOpeningRequestPersistenceEntity(customer))
                .thenReturn(persistenceEntity);
        var persistenceException = new DataIntegrityViolationException(
                "Unique constraint violation",
                new ConstraintViolationException(
                        "Unique constraint violation",
                        new SQLException("duplicate key", "23505"),
                        "uk_customer_account_opening_email"
                )
        );
        when(jpaRepository.save(persistenceEntity)).thenThrow(persistenceException);

        var exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> customerRepository.save(customer)
        );

        assertSame(persistenceException, exception);
    }
}
