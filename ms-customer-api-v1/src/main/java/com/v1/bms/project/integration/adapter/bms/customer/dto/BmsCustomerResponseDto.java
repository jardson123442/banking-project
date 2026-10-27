package com.v1.bms.project.integration.adapter.bms.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "status", "createdAt"})
public class BmsCustomerResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -3929158699633774599L;

    private transient Object data;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}
