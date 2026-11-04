package com.v1.bms.project.configuration.common.config;

import com.v1.bms.project.application.port.input.CustomerAccountOpeningService;
import com.v1.bms.project.application.port.output.CustomerAccountRepository;
import com.v1.bms.project.domain.service.CustomerAccountOpeningServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private CustomerAccountRepository customerAccountRepository;

    @Bean
    public CustomerAccountOpeningService customerAccountOpeningService() {
        var customerservice =  new CustomerAccountOpeningServiceImpl();
        customerservice.setCustomerAccountRepository(this.customerAccountRepository);
        return customerservice;
    }

    @Autowired
    public void setCustomerRepository(@Qualifier("CustomerCustomJpaRepository") CustomerAccountRepository customerAccountRepository) {
        this.customerAccountRepository = customerAccountRepository;
    }

}
