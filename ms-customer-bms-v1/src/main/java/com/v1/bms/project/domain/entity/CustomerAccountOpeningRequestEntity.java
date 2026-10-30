package com.v1.bms.project.domain.entity;

import com.v1.bms.project.persistence.entity.CustomerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

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
