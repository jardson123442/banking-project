package com.v1.bms.project.integration.adapter.bms.customer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCustomerRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3407781861443463955L;

    @NotBlank
    private String name;

    @NotBlank
    private String cpf;

    @NotBlank
    private String birthDate;

    @NotBlank
    private String email;
}
