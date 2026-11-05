package com.v1.bms.project.domain.validation;

import com.v1.bms.project.domain.entity.CustomerEntity;
import com.v1.bms.project.domain.vo.exceptions.NullAndBlankException;
import com.v1.bms.project.domain.vo.exceptions.UnderageException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class CustomerValidator {

    private static final int MINIMUM_AGE = 18;
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public void validate(CustomerEntity customer) {
        if (customer == null) {
            throw new NullAndBlankException("Customer cannot be null");
        }

        validateRequiredField("Name", customer.getName());
        validateRequiredField("CPF", customer.getCpf());
        validateRequiredField("Birth date", customer.getBirthDate());
        validateRequiredField("Email", customer.getEmail());
        validateName(customer.getName());
        validateCpf(customer.getCpf());
        validateEmail(customer.getEmail());
        validateAge(customer.getBirthDate());
    }

    private void validateRequiredField(String fieldName, String value) {
        if (value == null || value.isBlank()) {
            throw new NullAndBlankException(fieldName + " cannot be null or blank");
        }
    }

    private void validateName(String name) {
        var normalizedName = name.trim();
        if (normalizedName.length() < 2 || normalizedName.length() > 100) {
            throw new IllegalArgumentException("Name must be between 2 and 100 characters");
        }
        if (!normalizedName.matches("^[A-Za-zÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Name must contain only letters and spaces");
        }
    }

    private void validateCpf(String cpf) {
        var normalizedCpf = cpf.trim();
        if (!normalizedCpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF must contain exactly 11 digits");
        }
    }

    private void validateEmail(String email) {
        var normalizedEmail = email.trim();
        if (!normalizedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validateAge(String birthDateValue) {
        final LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birthDateValue.trim(), BIRTH_DATE_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Birth date must follow the format dd/MM/yyyy", exception);
        }

        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid birth date");
        }

        if (Period.between(birthDate, LocalDate.now()).getYears() < MINIMUM_AGE) {
            throw new UnderageException("Customer must be at least " + MINIMUM_AGE + " years old");
        }
    }
}
