package com.v1.bms.project.application;

import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateResponseDTO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;

public interface CustomerAccountOpeningFacade {

    CustomerAccountOpeningCreateResponseDTO create(CustomerAccountOpeningCreateVO createVO);

    CustomerAccountOpeningCreateResponseDTO findOwner(String cpf);
}
