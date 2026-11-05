package com.v1.bms.project.domain.vo.account.opening;

import com.v1.bms.project.domain.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResultVO {
    private String id;
    private String cpf;
    private String name;
    private CustomerStatus status;
}
