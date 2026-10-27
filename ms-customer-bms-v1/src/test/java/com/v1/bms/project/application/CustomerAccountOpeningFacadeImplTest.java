package com.v1.bms.project.application;

import com.v1.bms.project.api.assembler.CustomerAccountOpeningAssembler;
import com.v1.bms.project.api.disassembler.CustomerDisassembler;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateResponseDTO;
import com.v1.bms.project.domain.service.CustomerAccountOpeningService;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningResultVO;
import com.v1.bms.project.persistence.entity.CustomerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerAccountOpeningFacadeImplTest {


    @Mock
    private CustomerAccountOpeningService customerAccountOpeningService;

    @Mock
    private CustomerAccountOpeningAssembler customerAccountOpeningAssembler;

    @Mock
    private CustomerDisassembler customerDisassembler;

    @InjectMocks
    private CustomerAccountOpeningFacadeImpl customerAccountOpeningFacade;

    private CustomerAccountOpeningCreateRequestDTO createCustomerRequestDto;
    private CustomerAccountOpeningCreateVO createCustomerVO;
    private CustomerAccountOpeningResultVO createCustomerResultVO;

    private String requestId = "requestId";
    private String cpf = "12345678901";
    private String name = "John Doe";
    private String email = "john.doe@example.com";
    private String birthDate = "1990-01-01";
    private String underageBirthDate = "2010-01-01";
    private CustomerStatus status = CustomerStatus.ACTIVE;

    private final String CREATED_STATUS = "Created";
    private final String CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE = "Customer Account Opening created successfully";

    @BeforeEach
    void setUp() {
        createCustomerRequestDto = CustomerAccountOpeningCreateRequestDTO.builder()
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .build();

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
    }

    @Test
    @DisplayName("Deve criar uma conta de cliente com sucesso")
    void testCreateCustomerAccountOpening() {
        when(customerDisassembler.toDomainObject(
                createCustomerRequestDto,
                CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE,
                CREATED_STATUS
        )).thenReturn(createCustomerVO);
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

        var response = customerAccountOpeningFacade.create(createCustomerRequestDto);

        assertEquals(CREATED_STATUS, response.getStatus());
        assertEquals(CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE, response.getMessage());
        assertEquals(createCustomerResultVO, response.getData());
    }
}
