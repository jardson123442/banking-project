package com.v1.bms.project.domain.vo.account.opening;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountOpeningCreateVO {
    private String name;
    private String cpf;
    private String birthDate;
    private String email;
}
