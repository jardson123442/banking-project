package com.v1.bms.project.domain.service;

import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import com.v1.bms.project.persistence.entity.CustomerAccountOpeningRequestPersistenceEntity;
import com.v1.bms.project.persistence.entity.CustomerStatus;
import com.v1.bms.project.persistence.repository.CustomerAccountOpeningRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerAccountOpeningServiceImplTest {

    @Mock
    private CustomerAccountOpeningRequestRepository customerAccountOpeningRequestRepository;

    @InjectMocks
    private CustomerAccountOpeningServiceImpl customerAccountOpeningService;

    @Test
    void createPersistsAndReturnsActiveStatus() {
        var createVO = CustomerAccountOpeningCreateVO.builder()
                .name("Customer")
                .cpf("12345678900")
                .birthDate("1990-01-01")
                .email("customer@example.com")
                .build();
        when(customerAccountOpeningRequestRepository.findByCpf(createVO.getCpf())).thenReturn(Optional.empty());
        when(customerAccountOpeningRequestRepository.save(any(CustomerAccountOpeningRequestPersistenceEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var result = customerAccountOpeningService.create(createVO);

        var persistedRequest = ArgumentCaptor.forClass(CustomerAccountOpeningRequestPersistenceEntity.class);
        verify(customerAccountOpeningRequestRepository).save(persistedRequest.capture());
        assertEquals(CustomerStatus.ACTIVE, persistedRequest.getValue().getStatus());
        assertEquals(CustomerStatus.ACTIVE, result.getStatus());
    }
}
