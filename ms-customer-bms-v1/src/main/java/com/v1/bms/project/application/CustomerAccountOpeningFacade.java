package com.v1.bms.project.application;

import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateRequestDTO;
import com.v1.bms.project.configuration.common.dto.CustomerAccountOpeningCreateResponseDTO;

public interface CustomerAccountOpeningFacade {

    CustomerAccountOpeningCreateResponseDTO create(CustomerAccountOpeningCreateRequestDTO customerAccountOpeningCreateRequestDTO);

    CustomerAccountOpeningCreateResponseDTO findOwner(String cpf);
}
