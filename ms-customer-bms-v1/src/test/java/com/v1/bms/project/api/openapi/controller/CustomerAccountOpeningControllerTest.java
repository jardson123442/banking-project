package com.v1.bms.project.api.openapi.controller;

import com.v1.bms.project.application.CustomerAccountOpeningFacade;
import com.v1.bms.project.api.controller.CustomerAccountOpeningController;
import com.v1.bms.project.api.disassembler.CustomerDisassembler;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateResponseDTO;
import com.v1.bms.project.domain.enums.CustomerStatus;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningResultVO;
import com.v1.bms.project.domain.vo.exceptions.CustomerAccountNotFoundException;
import com.v1.bms.project.domain.vo.exceptions.CustomerCpfAlreadyInUse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static com.v1.bms.project.api.UrlConstant.CUSTOMER_ACCOUNT_FIND_OWNER_URL;
import static com.v1.bms.project.api.UrlConstant.CUSTOMER_ACCOUNT_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerAccountOpeningController.class)
public class CustomerAccountOpeningControllerTest {

    @MockitoBean
    private CustomerAccountOpeningFacade customerAccountOpeningFacade;

    @MockitoBean
    private CustomerDisassembler customerDisassembler;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerAccountOpeningCreateRequestDTO createCustomerRequestDto;

    private String requestId = "requestId";
    private String cpf = "12345678901";
    private String name = "John Doe";
    private String email = "john.doe@example.com";
    private String birthDate = "1990-01-01";
    private String underageBirthDate = "2010-01-01";
    private CustomerStatus status = CustomerStatus.ACTIVE;

    @BeforeEach
    void setUp() {
        createCustomerRequestDto = CustomerAccountOpeningCreateRequestDTO.builder()
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .build();

    }
    @Test
    @DisplayName("Deve criar um CustomerAccountOpening com sucesso")
    void shouldCreateCustomerAccountOpeningSuccessfully() throws Exception {
        var customerAccountOpeningCreateResponseDTO = CustomerAccountOpeningCreateResponseDTO.builder()
                .status("Created")
                .data(CustomerAccountOpeningResultVO.builder()
                        .id(requestId)
                        .cpf(cpf)
                        .name(name)
                        .status(status)
                        .build())
                .message("Customer Account Opening created successfully")
                .createdAt(LocalDateTime.now())
                .build();


        var createVO = CustomerAccountOpeningCreateVO.builder()
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .build();
        when(customerDisassembler.toDomainObject(any(CustomerAccountOpeningCreateRequestDTO.class)))
                .thenReturn(createVO);
        when(customerAccountOpeningFacade.create(createVO))
                .thenReturn(customerAccountOpeningCreateResponseDTO);

        mockMvc.perform(
                        post(CUSTOMER_ACCOUNT_URL)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCustomerRequestDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value("requestId"))
                .andExpect(jsonPath("$.data.cpf").value("12345678901"))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.status").value(CustomerStatus.ACTIVE.name()))
                .andExpect(jsonPath("$.message").value("Customer Account Opening created successfully"));

        var requestCaptor = ArgumentCaptor.forClass(CustomerAccountOpeningCreateRequestDTO.class);
        verify(customerDisassembler).toDomainObject(requestCaptor.capture());
        assertEquals(birthDate, requestCaptor.getValue().getBirthDate());
        verify(customerAccountOpeningFacade).create(createVO);
    }

    @Test
    @DisplayName("Deve retornar erro ao criar conta com idade inferior a 18 anos")
    void shouldReturnErrorWhenCreatingCustomerAccountOpeningWithUnderage() throws Exception {

        var createRequestDTO = CustomerAccountOpeningCreateRequestDTO.builder()
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(underageBirthDate)
                .build();

        var createVO = CustomerAccountOpeningCreateVO.builder()
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(underageBirthDate)
                .build();
        when(customerDisassembler.toDomainObject(createRequestDTO)).thenReturn(createVO);
        when(customerAccountOpeningFacade.create(createVO))
                .thenThrow(new IllegalArgumentException("Customer must be at least 18 years old"));

        mockMvc.perform(
                        post(CUSTOMER_ACCOUNT_URL)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequestDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer must be at least 18 years old"));
    }

    @Test
    @DisplayName("Deve retornar erro ao criar um CustomerAccountOpening com CPF já existente")
    void shouldReturnErrorWhenCreatingCustomerAccountOpeningWithExistingCpf() throws Exception {

        var createVO = CustomerAccountOpeningCreateVO.builder()
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .build();
        when(customerDisassembler.toDomainObject(any(CustomerAccountOpeningCreateRequestDTO.class)))
                .thenReturn(createVO);
        when(customerAccountOpeningFacade.create(createVO))
                .thenThrow(new CustomerCpfAlreadyInUse("CPF is already used: " + cpf));

        mockMvc.perform(
                        post(CUSTOMER_ACCOUNT_URL)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCustomerRequestDto))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("CPF is already used: " + cpf));

        verify(customerAccountOpeningFacade).create(createVO);
    }

    @Test
    @DisplayName("Deve retornar um customer buscando pelo CPF")
    void shouldReturnCustomerAccountOpeningByCpf() throws Exception {
        var customerAccountOpeningCreateResponseDTO = CustomerAccountOpeningCreateResponseDTO.builder()
                .status("Found")
                .data(CustomerAccountOpeningResultVO.builder()
                        .id(requestId)
                        .cpf(cpf)
                        .name(name)
                        .status(status)
                        .build())
                .message("Customer Account Opening found")
                .createdAt(LocalDateTime.now())
                .build();

        when(customerAccountOpeningFacade.findOwner(cpf))
                .thenReturn(customerAccountOpeningCreateResponseDTO);

        mockMvc.perform(
                        get(CUSTOMER_ACCOUNT_FIND_OWNER_URL, cpf)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCustomerRequestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("requestId"))
                .andExpect(jsonPath("$.data.cpf").value("12345678901"))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.status").value(CustomerStatus.ACTIVE.name()))
                .andExpect(jsonPath("$.message").value("Customer Account Opening found"));
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar um CustomerAccountOpening com CPF inexistente")
    void shouldReturnErrorWhenFindingCustomerAccountOpeningWithNonExistingCpf() throws Exception {
        when(customerAccountOpeningFacade.findOwner(cpf))
                .thenThrow(new CustomerAccountNotFoundException("No account opening request found for CPF: " + cpf));

        mockMvc.perform(
                        get(CUSTOMER_ACCOUNT_FIND_OWNER_URL, cpf)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCustomerRequestDto))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No account opening request found for CPF: " + cpf));

    }
}
