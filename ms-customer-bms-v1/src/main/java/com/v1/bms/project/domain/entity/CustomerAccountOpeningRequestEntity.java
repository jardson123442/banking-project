package com.v1.bms.project.domain.entity;

import com.v1.bms.project.domain.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountOpeningRequestEntity {
    private String id;
    private String name;
    private String cpf;
    private String birthDate;
    private String email;
    private CustomerStatus status;
    private LocalDateTime createdAt;
}
