package com.v1.bms.project.application;

import com.v1.bms.project.api.assembler.CustomerAccountOpeningAssembler;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateResponseDTO;
import com.v1.bms.project.domain.enums.CustomerStatus;
import com.v1.bms.project.domain.service.CustomerAccountOpeningService;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningResultVO;
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
    private CustomerAccountOpeningService customerAccountOpeningService;

    @Mock
    private CustomerAccountOpeningAssembler customerAccountOpeningAssembler;

    @InjectMocks
    private CustomerAccountOpeningFacadeImpl customerAccountOpeningFacade;

    private CustomerAccountOpeningCreateResponseDTO createCustomerResponseDto;
    private CustomerAccountOpeningCreateVO createCustomerVO;
    private CustomerAccountOpeningResultVO createCustomerResultVO;
    private CustomerAccountOpeningCreateRequestDTO createRequestDTO;

    private String requestId = "requestId";
    private String cpf = "12345678901";
    private String name = "John Doe";
    private String email = "john.doe@example.com";
    private String birthDate = "1990-01-01";
    private String underageBirthDate = "2010-01-01";
    private CustomerStatus status = CustomerStatus.ACTIVE;

    private final String CREATED_STATUS = "Created";
    private final String CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE = "Customer Account Opening created successfully";
    private final String CUSTOMER_ACCOUNT_FOUND_MESSAGE = "Customer Account Opening found";
    private final String FOUND_STATUS = "Found";

    @BeforeEach
    void setUp() {
        createCustomerVO = CustomerAccountOpeningCreateVO.builder()
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .build();

        createCustomerResultVO = CustomerAccountOpeningResultVO.builder()
                .id(requestId)
                .cpf(cpf)
                .name(name)
                .status(status)
                .build();

        createCustomerResponseDto = CustomerAccountOpeningCreateResponseDTO.builder()
                .data(createCustomerResultVO)
                .message(CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE)
                .status(CREATED_STATUS)
                .createdAt(LocalDateTime.now())
                .build();

        createRequestDTO = CustomerAccountOpeningCreateRequestDTO.builder()
                .cpf(cpf)
                .name(name)
                .birthDate(birthDate)
                .email(email)
                .build();
    }

    @Test
    @DisplayName("Deve criar uma conta de cliente com sucesso")
    void testCreateCustomerAccountOpening() {
        when(customerAccountOpeningService.create(createCustomerVO)).thenReturn(createCustomerResultVO);
        var expectedResponse = CustomerAccountOpeningCreateResponseDTO.builder()
                .status(CREATED_STATUS)
                .data(createCustomerResultVO)
                .message(CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE)
                .createdAt(LocalDateTime.now())
                .build();
        when(customerAccountOpeningAssembler.toModel(
                createCustomerResultVO,
                CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE,
                CREATED_STATUS
        )).thenReturn(expectedResponse);

        var response = customerAccountOpeningFacade.create(createCustomerVO);

        assertEquals(CREATED_STATUS, response.getStatus());
        assertEquals(CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE, response.getMessage());
        assertEquals(createCustomerResultVO, response.getData());
    }

    @Test
    @DisplayName("Deve retornar customer buscando pelo CPF")
    void shouldReturnCustomerByCpf() {
        when(customerAccountOpeningService.findOwner(cpf)).thenReturn(createCustomerResultVO);
        when(customerAccountOpeningAssembler.toModel(createCustomerResultVO, CUSTOMER_ACCOUNT_FOUND_MESSAGE, FOUND_STATUS)).thenReturn(createCustomerResponseDto);

        var result = customerAccountOpeningFacade.findOwner(cpf);

        assertEquals(createCustomerResponseDto, result);
        verify(customerAccountOpeningService).findOwner(cpf);
        verify(customerAccountOpeningAssembler).toModel(createCustomerResultVO, CUSTOMER_ACCOUNT_FOUND_MESSAGE, FOUND_STATUS);

        assertEquals(createCustomerResponseDto, result);
        assertEquals(createCustomerResponseDto.getData(), result.getData());
        assertEquals(createCustomerResponseDto.getMessage(), result.getMessage());
        assertEquals(createCustomerResponseDto.getStatus(), result.getStatus());
        assertEquals(createCustomerResponseDto.getCreatedAt(), result.getCreatedAt());
    }
}
