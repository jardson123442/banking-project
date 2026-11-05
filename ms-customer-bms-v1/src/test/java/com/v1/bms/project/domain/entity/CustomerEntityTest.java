package com.v1.bms.project.domain.entity;

import com.v1.bms.project.domain.enums.CustomerStatus;
import com.v1.bms.project.domain.vo.exceptions.NullAndBlankException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerEntityTest {

    private static final String ID = "customer-id";
    private static final String NAME = "John Doe";
    private static final String CPF = "12312312312";
    private static final String BIRTH_DATE = "11/12/1998";
    private static final String EMAIL = "john.doe@example.com";
    private static final CustomerStatus STATUS = CustomerStatus.ACTIVE;
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2025, 1, 1, 12, 0);

    private CustomerEntity customerEntity;

    @BeforeEach
    void setUp() {
        customerEntity = CustomerEntity.builder()
                .id(ID)
                .name(NAME)
                .cpf(CPF)
                .birthDate(BIRTH_DATE)
                .email(EMAIL)
                .status(STATUS)
                .createdAt(CREATED_AT)
                .build();
    }
    @Test
    @DisplayName("Deve criar um cliente com sucesso")
    void shouldCreateCustomerSuccessfully() {
        var customer = new CustomerEntity(
                ID, NAME, CPF, BIRTH_DATE, EMAIL, STATUS, CREATED_AT);

        assertEquals(ID, customer.getId());
        assertEquals(NAME, customer.getName());
        assertEquals(CPF, customer.getCpf());
        assertEquals(BIRTH_DATE, customer.getBirthDate());
        assertEquals(EMAIL, customer.getEmail());
        assertEquals(STATUS, customer.getStatus());
        assertEquals(CREATED_AT, customer.getCreatedAt());
    }

    @Test
    @DisplayName("Deve manter campos inválidos para validação pelo serviço")
    void shouldKeepInvalidFieldsForServiceValidation() {
        var customer = new CustomerEntity(null, "", "", "", "", null, null);

        assertAll(
                () -> assertEquals("", customer.getName()),
                () -> assertEquals("", customer.getCpf()),
                () -> assertEquals("", customer.getBirthDate()),
                () -> assertEquals("", customer.getEmail()),
                () -> assertNull(customer.getStatus())
        );
    }

    @Test
    @DisplayName("Deve atualizar nome, cpf e email válido removendo espaços externos")
    void shouldSetTrimmedName() {
        var customer = customerEntity;

        customer.setName("  Jane Doe  ");

        assertEquals("Jane Doe", customer.getName());
    }

    @Test
    @DisplayName("Deve rejeitar nome nulo, em branco ou fora do limite")
    void shouldRejectInvalidName() {
        var customer = customerEntity;

        assertAll(
                () -> assertThrows(NullAndBlankException.class, () -> customer.setName(null)),
                () -> assertThrows(NullAndBlankException.class, () -> customer.setName("   ")),
                () -> assertThrows(IllegalArgumentException.class, () -> customer.setName(" A ")),
                () -> assertThrows(IllegalArgumentException.class, () -> customer.setName("a".repeat(101)))
        );
    }

    @Test
    @DisplayName("Deve atualizar CPF válido removendo espaços externos")
    void shouldSetTrimmedCpf() {
        var customer = customerEntity;

        customer.setCpf("  98765432100  ");

        assertEquals("98765432100", customer.getCpf());
    }

    @Test
    @DisplayName("Deve rejeitar CPF nulo, em branco ou com tamanho inválido")
    void shouldRejectInvalidCpf() {
        var customer = customerEntity;

        assertAll(
                () -> assertThrows(NullAndBlankException.class, () -> customer.setCpf(null)),
                () -> assertThrows(NullAndBlankException.class, () -> customer.setCpf("   ")),
                () -> assertThrows(IllegalArgumentException.class, () -> customer.setCpf("1234567890")),
                () -> assertThrows(IllegalArgumentException.class, () -> customer.setCpf("123456789012"))
        );
    }

    @Test
    @DisplayName("Deve atualizar email válido removendo espaços externos")
    void shouldSetTrimmedEmail() {
        var customer = customerEntity;

        customer.setEmail(" carlos@gmail.com ");

        assertEquals("carlos@gmail.com", customer.getEmail());
    }

    @Test
    @DisplayName("Deve rejeitar email nulo, em branco ou com formato inválido")
    void shouldRejectInvalidEmail() {
        var customer = customerEntity;

        assertAll(
                () -> assertThrows(NullAndBlankException.class, () -> customer.setEmail(null)),
                () -> assertThrows(NullAndBlankException.class, () -> customer.setEmail("   ")),
                () -> assertThrows(IllegalArgumentException.class, () -> customer.setEmail("invalid-email")),
                () -> assertThrows(IllegalArgumentException.class, () -> customer.setEmail("invalid@.com")),
                () -> assertThrows(IllegalArgumentException.class, () -> customer.setEmail("invalid@domain"))
        );
    }
}
