package com.v1.bms.project.api.openapi.controller;

import com.v1.bms.project.application.facade.CustomerFacade;
import com.v1.bms.project.dummies.CustomerDummy;
import com.v1.bms.project.integration.adapter.bms.customer.dto.CreateCustomerRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.v1.bms.project.api.UrlConstant.CUSTOMER_ACCOUNT_API_URL;
import static com.v1.bms.project.dummies.CustomerDummy.cpf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "integration.customer-bms.base-url=http://localhost:8083"
)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private CustomerFacade customerFacade;

    @Test
    @DisplayName("Deve retornar 201 quando criar uma conta com sucesso")
    void shouldCreateCustomerAccountWithSuccess() throws Exception {
        var customerRequest = CreateCustomerRequestDTO.builder()
                .name(CustomerDummy.name)
                .cpf(cpf)
                .birthDate(CustomerDummy.birthDate)
                .email(CustomerDummy.email)
                .build();
        doNothing().when(customerFacade).create(customerRequest);

        mockMvc.perform(post(CUSTOMER_ACCOUNT_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "cpf": "%s",
                                  "birthDate": "%s",
                                  "email": "%s"
                                }
                                """.formatted(
                                CustomerDummy.name,
                                cpf,
                                CustomerDummy.birthDate,
                                CustomerDummy.email)))
                .andExpect(status().isCreated());

        verify(customerFacade).create(customerRequest);
    }

    @Test
    @DisplayName("Deve retornar os dados da conta pelo CPF")
    void shouldFindCustomerByCpf() throws Exception {
        var customerResponse = CustomerDummy.createCustomerResponseDTO();
        doReturn(customerResponse).when(customerFacade).findCustomerByCpf(cpf);

        mockMvc.perform(get(CUSTOMER_ACCOUNT_API_URL + "/" + cpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(customerResponse.getMessage()))
                .andExpect(jsonPath("$.status").value(customerResponse.getStatus()));

        verify(customerFacade).findCustomerByCpf(cpf);
    }

    @Test
    @DisplayName("Deve retornar 400 quando os dados obrigatórios estiverem vazios")
    void shouldRejectCustomerAccountWithMissingRequiredFields() throws Exception {
        mockMvc.perform(post(CUSTOMER_ACCOUNT_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
