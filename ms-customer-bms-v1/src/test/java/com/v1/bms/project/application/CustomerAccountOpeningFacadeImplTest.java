package com.v1.bms.project.application;

import com.v1.bms.project.api.assembler.CustomerAccountOpeningAssembler;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.domain.service.CustomerAccountOpeningService;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningResultVO;
import com.v1.bms.project.persistence.entity.CustomerStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerAccountOpeningFacadeImplTest {

    @Mock
    private CustomerAccountOpeningMapper customerAccountOpeningMapper;

    @Mock
    private CustomerAccountOpeningService customerAccountOpeningService;

    @InjectMocks
    private CustomerAccountOpeningFacadeImpl customerAccountOpeningFacade;

    @Test
    void createReturnsCustomerStatusInResponseData() {
        var request = CustomerAccountOpeningCreateRequestDTO.builder().build();
        var createVO = CustomerAccountOpeningCreateVO.builder().build();
        var result = CustomerAccountOpeningResultVO.builder()
                .status(CustomerStatus.ACTIVE)
                .build();
        when(customerAccountOpeningMapper.toCustomerAccountOpeningCreateVO(request)).thenReturn(createVO);
        when(customerAccountOpeningService.create(createVO)).thenReturn(result);

        var response = customerAccountOpeningFacade.create(request);

        var responseData = (CustomerAccountOpeningResultVO) response.getData();
        assertEquals(CustomerStatus.ACTIVE, responseData.getStatus());
    }
}
