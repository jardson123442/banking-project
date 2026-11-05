package com.v1.bms.project.application;

import com.v1.bms.project.api.assembler.CustomerAssembler;
import com.v1.bms.project.api.disassembler.CustomerDisassembler;
import com.v1.bms.project.api.dto.CustomerRequestDTO;
import com.v1.bms.project.api.dto.CustomerResponseDTO;
import com.v1.bms.project.domain.enums.CustomerStatus;
import com.v1.bms.project.application.port.input.CustomerService;
import com.v1.bms.project.domain.entity.CustomerEntity;
import com.v1.bms.project.domain.vo.account.opening.CustomerResultVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerAccountOpeningFacadeImplTest {


    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerAssembler customerAssembler;

    @Mock
    private CustomerDisassembler customerDisassembler;

    @InjectMocks
    private CustomerFacadeImpl customerAccountOpeningFacade;

    private CustomerResponseDTO createCustomerResponseDto;
    private CustomerEntity createCustomerEntity;
    private CustomerResultVO createCustomerResultVO;
    private CustomerRequestDTO createRequestDTO;

    private String requestId = "requestId";
    private String cpf = "12345678901";
    private String name = "John Doe";
    private String email = "john.doe@example.com";
    private String birthDate = "1990-01-01";
    private String underageBirthDate = "2010-01-01";
    private CustomerStatus status = CustomerStatus.ACTIVE;

    private final String CREATED_STATUS = "Created";
    private final String CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE = "Customer created successfully";
    private final String CUSTOMER_ACCOUNT_FOUND_MESSAGE = "Customer Account Opening found";
    private final String FOUND_STATUS = "Found";

    @BeforeEach
    void setUp() {
        createCustomerEntity = CustomerEntity.builder()
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .status(status)
                .build();

        createCustomerResultVO = CustomerResultVO.builder()
                .id(requestId)
                .cpf(cpf)
                .name(name)
                .status(status)
                .build();

        createCustomerResponseDto = CustomerResponseDTO.builder()
                .data(createCustomerResultVO)
                .message(CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE)
                .status(CREATED_STATUS)
                .createdAt(LocalDateTime.now())
                .build();

        createRequestDTO = CustomerRequestDTO.builder()
                .cpf(cpf)
                .name(name)
                .birthDate(birthDate)
                .email(email)
                .build();
    }

    @Test
    @DisplayName("Deve criar uma conta de cliente com sucesso")
    void testCreateCustomerAccountOpening() {
        var expectedResponseDto = CustomerResponseDTO.builder()
                .status(CREATED_STATUS)
                .data(createCustomerResultVO)
                .message(CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE)
                .createdAt(LocalDateTime.now())
                .build();

        when(customerAssembler.toModel(
                createCustomerResultVO,
                CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE,
                CREATED_STATUS
        )).thenReturn(expectedResponseDto);
        when(customerDisassembler.toEntity(createRequestDTO)).thenReturn(createCustomerEntity);
        when(customerService.create(createCustomerEntity)).thenReturn(createCustomerResultVO);

        var response = customerAccountOpeningFacade.create(createRequestDTO);

        assertEquals(CREATED_STATUS, response.getStatus());
        assertEquals(CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE, response.getMessage());
        assertEquals(createCustomerResultVO, response.getData());
        verify(customerService).create(createCustomerEntity);
    }

    @Test
    @DisplayName("Deve retornar customer buscando pelo CPF")
    void shouldReturnCustomerByCpf() {
        when(customerService.findOwner(cpf)).thenReturn(createCustomerResultVO);
        when(customerAssembler.toModel(createCustomerResultVO, CUSTOMER_ACCOUNT_FOUND_MESSAGE, FOUND_STATUS)).thenReturn(createCustomerResponseDto);

        var result = customerAccountOpeningFacade.findOwner(cpf);

        assertEquals(createCustomerResponseDto, result);
        verify(customerService).findOwner(cpf);
        verify(customerAssembler).toModel(createCustomerResultVO, CUSTOMER_ACCOUNT_FOUND_MESSAGE, FOUND_STATUS);

        assertEquals(createCustomerResponseDto, result);
        assertEquals(createCustomerResponseDto.getData(), result.getData());
        assertEquals(createCustomerResponseDto.getMessage(), result.getMessage());
        assertEquals(createCustomerResponseDto.getStatus(), result.getStatus());
        assertEquals(createCustomerResponseDto.getCreatedAt(), result.getCreatedAt());
    }
}
