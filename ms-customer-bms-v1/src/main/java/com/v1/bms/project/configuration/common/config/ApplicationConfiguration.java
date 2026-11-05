package com.v1.bms.project.configuration.common.config;

import com.v1.bms.project.application.port.input.CustomerService;
import com.v1.bms.project.application.port.output.CustomerRepository;
import com.v1.bms.project.domain.service.CustomerServiceImpl;
import com.v1.bms.project.domain.validation.CustomerValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public CustomerService customerAccountOpeningService(
            @Qualifier("CustomerCustomJpaRepository") CustomerRepository customerRepository,
            CustomerValidator customerValidator) {
        return new CustomerServiceImpl(customerRepository, customerValidator);
    }

    @Bean
    public CustomerValidator customerValidator() {
        return new CustomerValidator();
    }
}
