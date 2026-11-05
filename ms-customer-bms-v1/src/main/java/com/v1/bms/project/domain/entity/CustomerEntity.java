package com.v1.bms.project.domain.entity;

import com.v1.bms.project.domain.enums.CustomerStatus;
import com.v1.bms.project.domain.vo.exceptions.NullAndBlankException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CustomerEntity {
    private String id;
    private String name;
    private String cpf;
    private String birthDate;
    private String email;
    private CustomerStatus status;
    private LocalDateTime createdAt;

    @Builder
    public CustomerEntity(String id, String name, String cpf, String birthDate, String email, CustomerStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new NullAndBlankException("Name cannot be null or blank");
        }
        var normalizedName = name.trim();
        if (normalizedName.length() < 2 || normalizedName.length() > 100) {
            throw new IllegalArgumentException("Name must be between 2 and 100 characters");
        }
        if (!normalizedName.matches("^[A-Za-zÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Name must contain only letters and spaces");
        }
        this.name = normalizedName;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new NullAndBlankException("CPF cannot be null or blank");
        }
        var normalizedCpf = cpf.trim();
        if (!normalizedCpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF must contain exactly 11 digits");
        }
        this.cpf = normalizedCpf;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new NullAndBlankException("Email cannot be null or blank");
        }
        var normalizedEmail = email.trim();
        if (!normalizedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = normalizedEmail;
    }
}
