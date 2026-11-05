package com.v1.bms.project.domain.service;

import com.v1.bms.project.application.port.input.CustomerService;
import com.v1.bms.project.application.port.output.CustomerRepository;
import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.entity.CustomerEntity;
import com.v1.bms.project.domain.vo.account.opening.CustomerResultVO;
import com.v1.bms.project.domain.vo.exceptions.CustomerAccountNotFoundException;
import com.v1.bms.project.application.port.output.CustomerCpfAlreadyInUseException;
import com.v1.bms.project.domain.validation.CustomerValidator;

import java.util.Optional;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerValidator customerValidator;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerValidator customerValidator) {
        this.customerRepository = customerRepository;
        this.customerValidator = customerValidator;
    }

    @Override
    public CustomerResultVO create(CustomerEntity customer) {
        customerValidator.validate(customer);
        isCpfAlreadyRegistered(customer);
        var savedCustomer = persistCustomer(customer);

        return this.toCustomerResultVO(savedCustomer);
    }

    @Override
    public CustomerResultVO findOwner(String customerCpf) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "findOwned");

        var request = findCustomerByCpf(customerCpf)
                .orElseThrow(() -> new CustomerAccountNotFoundException("No customer found for CPF: " + customerCpf));

        return toCustomerResultVO(request);
    }

    private void isCpfAlreadyRegistered(CustomerEntity customer) {
        this.customerRepository
                .findByCpf(customer.getCpf())
                .ifPresent(existingRequest -> {
                    LogUtils.warn("CPF is already used: {}", customer.getCpf());
                    throw new CustomerCpfAlreadyInUseException("CPF is already used: " + existingRequest.getCpf());
                });
    }

    private Optional<CustomerEntity> findCustomerByCpf(String customerCpf) {
        return this.customerRepository.findByCpf(customerCpf);
    }

    private CustomerEntity persistCustomer(CustomerEntity request) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "persistCustomer");

        try {
            return this.customerRepository.save(request);
        } catch (CustomerCpfAlreadyInUseException exception) {
            throw new CustomerCpfAlreadyInUseException("CPF is already used: " + request.getCpf());
        }
    }

    private CustomerResultVO toCustomerResultVO(CustomerEntity requestEntity) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "toCustomerAccountOpeningResultVO");

        return CustomerResultVO.builder()
                .id(requestEntity.getId())
                .name(requestEntity.getName())
                .cpf(requestEntity.getCpf())
                .status(requestEntity.getStatus())
                .build();
    }

}
