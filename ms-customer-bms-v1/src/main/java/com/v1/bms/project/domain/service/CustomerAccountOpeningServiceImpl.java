package com.v1.bms.project.domain.service;

import com.v1.bms.project.configuration.common.rest.util.LogUtils;
import com.v1.bms.project.domain.entity.CustomerAccountOpeningRequestEntity;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningResultVO;
import com.v1.bms.project.domain.vo.exceptions.CustomerAccountNotFoundException;
import com.v1.bms.project.domain.vo.exceptions.CustomerCpfAlreadyInUse;
import com.v1.bms.project.domain.vo.exceptions.UnderageException;
import com.v1.bms.project.persistence.entity.CustomerStatus;
import com.v1.bms.project.persistence.repository.CustomerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.v1.bms.project.application.ApplicationConstants.CLASS_METHOD_MESSAGE_PATTERN;
import static com.v1.bms.project.application.ApplicationConstants.MINIMUM_AGE;

@Service
@RequiredArgsConstructor
public class CustomerAccountOpeningServiceImpl implements CustomerAccountOpeningService {

    private final CustomerAccountRepository customerAccountRepository;

    @Override
    public CustomerAccountOpeningResultVO create(CustomerAccountOpeningCreateVO createVO) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "create");

        validateExistingRequest(createVO);
        validateAge(createVO);

        var request = newRequest(createVO);
        var openedRequestEntity = persistRequest(request);
        return toCustomerAccountOpeningResultVO(openedRequestEntity);
    }

    //A validação de erro deixamos na operação ou criamos um metodo como feito acima.
    @Override
    public CustomerAccountOpeningResultVO findOwner(String customerCpf) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "findOwned");

        var request = findRequest(customerCpf)
                .orElseThrow(() -> new CustomerAccountNotFoundException("No account opening request found for CPF: " + customerCpf));

        return toCustomerAccountOpeningResultVO(request);
    }

    public void validateAge(CustomerAccountOpeningCreateVO createVO) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "validateAge");

        var birthDate = LocalDate.parse(createVO.getBirthDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        var today = LocalDate.now();

        if (birthDate.isAfter(today)) {
            LogUtils.warn("Invalid birth date: {}", birthDate);
            throw new IllegalArgumentException("Invalid birth date");
        }

        var age = Period.between(birthDate, today).getYears();

        if (age < MINIMUM_AGE) {
            LogUtils.warn("Customer is underage: {}", age);
            throw new UnderageException("Customer must be at least " + MINIMUM_AGE + " years old");
        }
    }
    private void validateExistingRequest(CustomerAccountOpeningCreateVO createVO) {
        customerAccountRepository
                .findByCpf(createVO.getCpf())
                .ifPresent(existingRequest -> {
                    LogUtils.warn("CPF is already used: {}", createVO.getCpf());
                    throw cpfExisting(existingRequest);
                });
    }

    private Optional<CustomerAccountOpeningRequestEntity> findRequest(String customerCpf) {
        return customerAccountRepository.findByCpf(customerCpf);
    }

    private CustomerCpfAlreadyInUse cpfExisting(CustomerAccountOpeningRequestEntity existingRequest) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "replayExistingRequest");

        return new CustomerCpfAlreadyInUse("CPF is already used: " + existingRequest.getCpf());
    }

    private CustomerAccountOpeningRequestEntity persistRequest(CustomerAccountOpeningRequestEntity request) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "persistRequest");

        try {
            return customerAccountRepository.save(request);
        } catch (DataIntegrityViolationException concurrentRequest) {
            LogUtils.warn("Account opening violates a database constraint: {}", concurrentRequest.getMostSpecificCause().getMessage());
            throw new CustomerAccountNotFoundException("Account opening conflicts with existing data");
        }
    }

    private CustomerAccountOpeningResultVO toCustomerAccountOpeningResultVO(CustomerAccountOpeningRequestEntity requestEntity) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "toCustomerAccountOpeningResultVO");

        return CustomerAccountOpeningResultVO.builder()
                .id(requestEntity.getId())
                .name(requestEntity.getName())
                .cpf(requestEntity.getCpf())
                .status(requestEntity.getStatus())
                .build();
    }

    private CustomerAccountOpeningRequestEntity newRequest(CustomerAccountOpeningCreateVO createVO) {
        LogUtils.log(CLASS_METHOD_MESSAGE_PATTERN, this.getClass().getSimpleName(), "newRequest");

        return CustomerAccountOpeningRequestEntity.builder()
                .name(createVO.getName())
                .cpf(createVO.getCpf())
                .birthDate(createVO.getBirthDate())
                .email(createVO.getEmail())
                .status(CustomerStatus.ACTIVE)
                .build();
    }

}
