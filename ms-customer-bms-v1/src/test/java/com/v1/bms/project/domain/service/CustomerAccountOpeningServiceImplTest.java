package com.v1.bms.project.domain.service;

import com.v1.bms.project.domain.entity.CustomerAccountOpeningRequestEntity;
import com.v1.bms.project.domain.enums.CustomerStatus;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningResultVO;
import com.v1.bms.project.domain.vo.exceptions.CustomerAccountNotFoundException;
import com.v1.bms.project.domain.vo.exceptions.CustomerCpfAlreadyInUse;
import com.v1.bms.project.domain.vo.exceptions.UnderageException;
import com.v1.bms.project.persistence.repository.jpa.CustomerAccountOpeningRequestRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerAccountOpeningServiceImplTest {

    @Mock
    private CustomerAccountOpeningRequestRepositoryImpl customerAccountOpeningRequestRepository;

    @InjectMocks
    private CustomerAccountOpeningServiceImpl customerAccountOpeningService;

    private String requestId = "requestId";
    private String cpf = "12345678901";
    private String cpfEmpty = "";
    private String name = "John Doe";
    private String email = "john.doe@example.com";
    private String birthDate = "01/01/1990";
    private String underageBirthDate = "01/01/2010";
    private CustomerStatus status = CustomerStatus.ACTIVE;

    private final String CREATED_STATUS = "Created";
    private final String CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE = "Customer Account Opening created successfully";
    private final String CUSTOMER_ACCOUNT_FOUND_MESSAGE = "Customer Account Opening found";
    private final String FOUND_STATUS = "Found";

    private CustomerAccountOpeningCreateVO createCustomerVO;
    private CustomerAccountOpeningRequestEntity customerEntity;

    @BeforeEach
    void setUp() {
        createCustomerVO = CustomerAccountOpeningCreateVO.builder()
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .build();

        customerEntity = CustomerAccountOpeningRequestEntity.builder()
                .id(requestId)
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .status(CustomerStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Deve criar uma conta de cliente com sucesso")
    void shouldCreateCustomerAccountOpening() {
        when(customerAccountOpeningRequestRepository.findByCpf(cpf))
                .thenReturn(Optional.empty());

        when(customerAccountOpeningRequestRepository.save(
                any(CustomerAccountOpeningRequestEntity.class)))
                .thenReturn(customerEntity);

        CustomerAccountOpeningResultVO response =
                customerAccountOpeningService.create(createCustomerVO);

        assertNotNull(response);
        assertEquals(requestId, response.getId());
        assertEquals(cpf, response.getCpf());
        assertEquals(name, response.getName());
        assertEquals(CustomerStatus.ACTIVE, response.getStatus());

        ArgumentCaptor<CustomerAccountOpeningRequestEntity> captor =
                ArgumentCaptor.forClass(CustomerAccountOpeningRequestEntity.class);

        verify(customerAccountOpeningRequestRepository).save(captor.capture());

        assertEquals(cpf, captor.getValue().getCpf());
        assertEquals(name, captor.getValue().getName());
        assertEquals(email, captor.getValue().getEmail());
        assertEquals(CustomerStatus.ACTIVE, captor.getValue().getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o CPF já estiver cadastrado")
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        when(customerAccountOpeningRequestRepository.findByCpf(cpf))
                .thenReturn(Optional.of(customerEntity));

        CustomerCpfAlreadyInUse exception = assertThrows(
                CustomerCpfAlreadyInUse.class,
                () -> customerAccountOpeningService.create(createCustomerVO)
        );

        assertTrue(exception.getMessage().contains(cpf));

        verify(customerAccountOpeningRequestRepository, never())
                .save(any(CustomerAccountOpeningRequestEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o cliente for menor de idade")
    void shouldThrowExceptionWhenCustomerIsUnderage() {
        createCustomerVO.setBirthDate(underageBirthDate);

        when(customerAccountOpeningRequestRepository.findByCpf(cpf))
                .thenReturn(Optional.empty());

        UnderageException exception = assertThrows(
                UnderageException.class,
                () -> customerAccountOpeningService.create(createCustomerVO)
        );

        assertTrue(exception.getMessage().contains("at least"));

        verify(customerAccountOpeningRequestRepository, never())
                .save(any(CustomerAccountOpeningRequestEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a data de nascimento for futura")
    void shouldThrowExceptionWhenBirthDateIsInTheFuture() {
        createCustomerVO.setBirthDate("01/01/2999");

        when(customerAccountOpeningRequestRepository.findByCpf(cpf))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerAccountOpeningService.create(createCustomerVO)
        );

        assertEquals("Invalid birth date", exception.getMessage());

        verify(customerAccountOpeningRequestRepository, never())
                .save(any(CustomerAccountOpeningRequestEntity.class));
    }

    @Test
    @DisplayName("Deve retornar cliente buscando pelo CPF")
    void shouldReturnCustomerByCpf() {
        when(customerAccountOpeningRequestRepository.findByCpf(cpf))
                .thenReturn(Optional.of(customerEntity));

        CustomerAccountOpeningResultVO response =
                customerAccountOpeningService.findOwner(cpf);

        assertNotNull(response);
        assertEquals(requestId, response.getId());
        assertEquals(cpf, response.getCpf());
        assertEquals(name, response.getName());
        assertEquals(CustomerStatus.ACTIVE, response.getStatus());

        verify(customerAccountOpeningRequestRepository).findByCpf(cpf);
        verify(customerAccountOpeningRequestRepository, never())
                .save(any(CustomerAccountOpeningRequestEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando não encontrar cliente pelo CPF")
    void shouldThrowExceptionWhenCustomerIsNotFound() {
        when(customerAccountOpeningRequestRepository.findByCpf(cpf))
                .thenReturn(Optional.empty());

        CustomerAccountNotFoundException exception = assertThrows(
                CustomerAccountNotFoundException.class,
                () -> customerAccountOpeningService.findOwner(cpf)
        );

        assertTrue(exception.getMessage().contains(cpf));
    }

    @Test
    @DisplayName("Deve tratar violação de integridade ao salvar")
    void shouldThrowExceptionWhenSaveViolatesDataIntegrity() {
        when(customerAccountOpeningRequestRepository.findByCpf(cpf))
                .thenReturn(Optional.empty());

        when(customerAccountOpeningRequestRepository.save(
                any(CustomerAccountOpeningRequestEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Constraint violation"));

        CustomerAccountNotFoundException exception = assertThrows(
                CustomerAccountNotFoundException.class,
                () -> customerAccountOpeningService.create(createCustomerVO)
        );

        assertEquals(
                "Account opening conflicts with existing data",
                exception.getMessage()
        );
    }
}
