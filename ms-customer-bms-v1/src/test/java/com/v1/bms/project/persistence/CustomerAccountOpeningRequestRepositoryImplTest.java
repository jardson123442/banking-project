package com.v1.bms.project.persistence;

import com.v1.bms.project.domain.entity.CustomerAccountOpeningRequestEntity;
import com.v1.bms.project.domain.enums.CustomerStatus;
import com.v1.bms.project.persistence.entity.CustomerAccountOpeningRequestPersistenceEntity;
import com.v1.bms.project.persistence.repository.jpa.CustomerAccountJpaRepository;
import com.v1.bms.project.persistence.repository.jpa.CustomerAccountOpeningRequestRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
        "spring.flyway.enabled=true",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ActiveProfiles("test")
class CustomerAccountOpeningRequestRepositoryImplTest {

    @Autowired
    private CustomerAccountJpaRepository repository;

    @Test
    @DisplayName("Deve persistir e buscar uma solicitação pelo CPF")
    void shouldSaveAndFindByCpf() {
        var request =
                CustomerAccountOpeningRequestPersistenceEntity.builder()
                        .name("Davi")
                        .cpf("91209419142")
                        .birthDate("24/10/1991")
                        .email("davii@gmail.com")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build();

        CustomerAccountOpeningRequestPersistenceEntity savedRequest = repository.save(request);
        Optional<CustomerAccountOpeningRequestPersistenceEntity> result =
                repository.findByCpf("91209419142");

        assertTrue(result.isPresent());
        assertEquals(savedRequest.getId(), result.get().getId());
        assertEquals(request.getCpf(), result.get().getCpf());
        assertEquals(request.getName(), result.get().getName());
        assertEquals(request.getEmail(), result.get().getEmail());
        assertEquals(CustomerStatus.ACTIVE, result.get().getStatus());
    }

    @Test
    @DisplayName("Deve retornar vazio quando não existir solicitação para o CPF")
    void shouldReturnEmptyWhenCpfDoesNotExist() {
        Optional<CustomerAccountOpeningRequestPersistenceEntity> result =
                repository.findByCpf("00000000000");

        assertFalse(result.isPresent());
    }
}
