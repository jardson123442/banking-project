package com.v1.bms.project.application;

import com.v1.bms.project.api.dto.CustomerRequestDTO;
import com.v1.bms.project.api.dto.CustomerResponseDTO;

public interface CustomerFacade {

    CustomerResponseDTO create(CustomerRequestDTO createDto);

    CustomerResponseDTO findOwner(String cpf);
}
