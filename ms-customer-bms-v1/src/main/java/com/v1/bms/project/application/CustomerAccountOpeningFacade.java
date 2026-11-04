package com.v1.bms.project.application;

import com.v1.bms.project.api.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.api.dto.CustomerAccountOpeningCreateResponseDTO;
import com.v1.bms.project.domain.vo.account.opening.CustomerAccountOpeningCreateVO;

public interface CustomerAccountOpeningFacade {

    CustomerAccountOpeningCreateResponseDTO create(CustomerAccountOpeningCreateRequestDTO createDto);

    CustomerAccountOpeningCreateResponseDTO findOwner(String cpf);
}
