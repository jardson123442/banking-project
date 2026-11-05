package com.v1.bms.project.domain.service;

import com.v1.bms.project.application.port.output.CustomerRepository;
import com.v1.bms.project.domain.entity.CustomerEntity;
import com.v1.bms.project.domain.enums.CustomerStatus;
import com.v1.bms.project.domain.vo.account.opening.CustomerResultVO;
import com.v1.bms.project.domain.vo.exceptions.CustomerAccountNotFoundException;
import com.v1.bms.project.application.port.output.CustomerCpfAlreadyInUseException;
import com.v1.bms.project.domain.vo.exceptions.NullAndBlankException;
import com.v1.bms.project.domain.vo.exceptions.UnderageException;
import com.v1.bms.project.domain.validation.CustomerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerAccountOpeningServiceImplTest {

    private CustomerRepository customerRepository;
    private CustomerServiceImpl customerServiceImpl;
    private CustomerResultVO resultVO;
    private CustomerEntity customerEntity;


    private String requestId = "requestId";
    private String cpf = "12345678901";
    private String name = "John Doe";
    private String email = "john.doe@example.com";
    private String birthDate = "01/01/1990";
    private String underageBirthDate = "01/01/2010";
    private String futureBirthDate = "01/01/2999";
    private CustomerStatus status = CustomerStatus.ACTIVE;

    @BeforeEach
    void setUp() {
        this.customerRepository = mock(CustomerRepository.class);
        CustomerValidator customerValidator = new CustomerValidator();
        this.customerServiceImpl = new CustomerServiceImpl(customerRepository, customerValidator);

        customerEntity = CustomerEntity.builder()
                .id(requestId)
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .status(CustomerStatus.ACTIVE)
                .build();

        resultVO = CustomerResultVO.builder()
                .id(requestId)
                .cpf(cpf)
                .name(name)
                .status(status)
                .build();
    }

    @Test
    @DisplayName("Deve criar uma conta de cliente com sucesso")
    void shouldCreateCustomerWithSuccess() {
        var customerEntity = CustomerEntity.builder()
                .id(requestId)
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .status(CustomerStatus.ACTIVE)
                .build();

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);

        CustomerResultVO response = customerServiceImpl.create(customerEntity);

        assertEquals(resultVO, response);
        assertEquals(customerEntity.getId(), response.getId());
        assertEquals(customerEntity.getCpf(), response.getCpf());
        assertEquals(customerEntity.getName(), response.getName());
        assertEquals(customerEntity.getStatus(), response.getStatus());

        ArgumentCaptor<CustomerEntity> captor = ArgumentCaptor.forClass(CustomerEntity.class);

        verify(customerRepository).save(captor.capture());

        assertEquals(cpf, captor.getValue().getCpf());
        assertEquals(name, captor.getValue().getName());
        assertEquals(email, captor.getValue().getEmail());
        assertEquals(CustomerStatus.ACTIVE, captor.getValue().getStatus());
    }

    @Test
    @DisplayName("Deve rejeitar campos nulos ou em branco antes de consultar o repositório")
    void shouldRejectNullAndBlankRequiredFields() {
        var invalidCustomers = new CustomerEntity[]{
                createCustomerWith(null, cpf, birthDate, email),
                createCustomerWith("   ", cpf, birthDate, email),
                createCustomerWith(name, null, birthDate, email),
                createCustomerWith(name, "   ", birthDate, email),
                createCustomerWith(name, cpf, null, email),
                createCustomerWith(name, cpf, "   ", email),
                createCustomerWith(name, cpf, birthDate, null),
                createCustomerWith(name, cpf, birthDate, "   ")
        };

        for (var customer : invalidCustomers) {
            assertThrows(NullAndBlankException.class,
                    () -> customerServiceImpl.create(customer));
        }

        verifyNoInteractions(customerRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o CPF já estiver cadastrado")
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        var existingCustomer = CustomerEntity.builder()
                .id(requestId)
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .status(CustomerStatus.ACTIVE)
                .build();
        var newCustomer = CustomerEntity.builder()
                .id(requestId)
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .status(CustomerStatus.ACTIVE)
                .build();
        when(customerRepository.findByCpf(cpf))
                .thenReturn(Optional.of(existingCustomer));

        CustomerCpfAlreadyInUseException exception = assertThrows(
                CustomerCpfAlreadyInUseException.class,
                () -> customerServiceImpl.create(newCustomer)
        );

        assertEquals("CPF is already used: " + cpf, exception.getMessage());
        verify(customerRepository).findByCpf(cpf);
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Deve converter duplicidade reportada pela porta em erro de CPF já utilizado")
    void shouldMapPortDuplicateCpfExceptionToDomainException() {
        when(customerRepository.findByCpf(cpf))
                .thenReturn(Optional.empty());
        when(customerRepository.save(any(CustomerEntity.class)))
                .thenThrow(new CustomerCpfAlreadyInUseException("CPF uniqueness constraint violated"));

        var exception = assertThrows(
                CustomerCpfAlreadyInUseException.class,
                () -> customerServiceImpl.create(customerEntity)
        );

        assertEquals("CPF is already used: " + cpf, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o cliente for menor de idade")
    void shouldThrowExceptionWhenCustomerIsUnderage() {
        var customerEntity = CustomerEntity.builder()
                .id(requestId)
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(underageBirthDate)
                .status(CustomerStatus.ACTIVE)
                .build();

        UnderageException exception = assertThrows(
                UnderageException.class,
                () -> customerServiceImpl.create(customerEntity)
        );

        assertTrue(exception.getMessage().contains("Customer must be at least 18 years old"));

        verify(customerRepository, never())
                .save(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a data de nascimento for futura")
    void shouldThrowExceptionWhenBirthDateIsInTheFuture() {
        customerEntity = CustomerEntity.builder()
                .id(requestId)
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(futureBirthDate)
                .status(CustomerStatus.ACTIVE)
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerServiceImpl.create(customerEntity)
        );

        assertEquals("Invalid birth date", exception.getMessage());

        verify(customerRepository, never())
                .save(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Deve retornar cliente buscando pelo CPF")
    void shouldReturnCustomerByCpf() {
        when(customerRepository.findByCpf(cpf))
                .thenReturn(Optional.of(customerEntity));

        CustomerResultVO response = customerServiceImpl.findOwner(cpf);

        assertNotNull(response);
        assertEquals(requestId, response.getId());
        assertEquals(cpf, response.getCpf());
        assertEquals(name, response.getName());
        assertEquals(CustomerStatus.ACTIVE, response.getStatus());

        verify(customerRepository).findByCpf(cpf);
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando não encontrar cliente pelo CPF")
    void shouldThrowExceptionWhenCustomerIsNotFound() {
        when(customerRepository.findByCpf(cpf))
                .thenReturn(Optional.empty());

        CustomerAccountNotFoundException exception = assertThrows(
                CustomerAccountNotFoundException.class,
                () -> customerServiceImpl.findOwner(cpf)
        );

        assertTrue(exception.getMessage().contains(cpf));
    }

    private CustomerEntity createCustomerWith(String customerName, String customerCpf, String customerBirthDate, String customerEmail) {
        return CustomerEntity.builder()
                .id(requestId)
                .cpf(customerCpf)
                .name(customerName)
                .email(customerEmail)
                .birthDate(customerBirthDate)
                .status(CustomerStatus.ACTIVE)
                .build();
    }
}
