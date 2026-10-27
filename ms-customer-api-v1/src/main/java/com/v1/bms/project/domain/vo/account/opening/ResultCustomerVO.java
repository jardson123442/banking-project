package com.v1.bms.project.domain.vo.account.opening;

import com.v1.bms.project.integration.adapter.bms.customer.dto.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultCustomerVO {
    private String id;
    private String cpf;
    private String name;
    private CustomerStatus status;
}
