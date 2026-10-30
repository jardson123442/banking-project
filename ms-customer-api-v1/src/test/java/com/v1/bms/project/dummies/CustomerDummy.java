package com.v1.bms.project.dummies;

import com.v1.bms.project.domain.vo.account.opening.CreateCustomerVO;
import com.v1.bms.project.domain.vo.account.opening.ResultCustomerVO;
import com.v1.bms.project.integration.adapter.bms.customer.dto.BmsCustomerResponseDto;
import com.v1.bms.project.integration.adapter.bms.customer.dto.CreateCustomerRequestDTO;
import com.v1.bms.project.integration.adapter.bms.customer.dto.CustomerStatus;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class CustomerDummy {

    public static String requestId = "requestId";
    public static String cpf = "12345678901";
    public static String cpfEmpty = "";
    public static String name = "John Doe";
    public static String email = "john.doe@example.com";
    public static String birthDate = "01/01/1990";
    public static String underageBirthDate = "01/01/2010";
    public static String id = "eaec1eed-e277-4da6-aa0e-fe98fa10f220";

    public static final String CREATED_STATUS = "Created";
    public static final String CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE = "Customer Account Opening created successfully";
    public static final String CUSTOMER_ACCOUNT_FOUND_MESSAGE = "Customer Account Opening found";
    public static final String FOUND_STATUS = "Found";

    public static BmsCustomerResponseDto createCustomerResponseDTO() {
        return BmsCustomerResponseDto.builder()
                .data(createCustomerVO())
                .message(CUSTOMER_CREATED_SUCCESSFULLY_MESSAGE)
                .status(CREATED_STATUS)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static CreateCustomerVO createCustomerVO() {
        return CreateCustomerVO.builder()
                .cpf(cpf)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .build();
    }
}
