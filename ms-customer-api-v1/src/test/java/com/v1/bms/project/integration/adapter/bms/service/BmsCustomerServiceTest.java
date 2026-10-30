package com.v1.bms.project.integration.adapter.bms.service;

import com.v1.bms.project.api.assembler.CustomerAssembler;
import com.v1.bms.project.dummies.CustomerDummy;
import com.v1.bms.project.integration.adapter.bms.customer.client.BmsCustomerClient;
import com.v1.bms.project.integration.adapter.bms.customer.service.BmsCustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.v1.bms.project.dummies.CustomerDummy.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BmsCustomerServiceTest {

    @Mock
    private BmsCustomerService bmsCustomerService;

    @Mock
    private BmsCustomerClient bmsCustomerClient;

    private CustomerAssembler customerAssembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar os dados de customer com sucesso")
    void shouldReturnCustomerWithSuccess() {

        var resultVO = CustomerDummy.createCustomerVO();
        var mockResponseDTO = CustomerDummy.createCustomerResponseDTO();

        when(this.bmsCustomerClient.createCustomer(resultVO)).thenReturn(mockResponseDTO);

        var result = bmsCustomerClient.createCustomer(resultVO);

        assertEquals(resultVO, result.getData());
        assertEquals(CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE, result.getMessage());
        assertEquals(CREATED_STATUS, result.getStatus());
        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve retornar customer pelo cpf com sucesso")
    void shoulReturnCustomerWithCpf() {
        var mockResponseDTO = CustomerDummy.createCustomerResponseDTO();

        when(this.bmsCustomerClient.getCustomerByCpf(cpf)).thenReturn(mockResponseDTO);

        var result = this.bmsCustomerClient.getCustomerByCpf(cpf);

        assertEquals(mockResponseDTO, result);
        verify(bmsCustomerClient).getCustomerByCpf(cpf);
    }
}
