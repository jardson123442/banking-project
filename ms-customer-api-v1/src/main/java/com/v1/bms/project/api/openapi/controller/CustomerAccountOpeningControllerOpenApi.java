package com.v1.bms.project.api.openapi.controller;

import com.v1.bms.project.configuration.common.OpenAPIResponseCodes;
import com.v1.bms.project.integration.adapter.bms.customer.dto.BmsCustomerResponseDto;
import com.v1.bms.project.integration.adapter.bms.customer.dto.CreateCustomerRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customer Account Opening", description = "Endpoints for managing customer account opening")
public interface CustomerAccountOpeningControllerOpenApi {

    @Operation(
            summary   = "Create a new personal account opening request",
            responses = {
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.ACCEPTED_STATUS,
                            description  = "Accepted"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.BAD_REQUEST_STATUS,
                            description  = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.UNAUTHORIZED_STATUS,
                            description  = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.FORBIDDEN,
                            description  = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.NOT_FOUND_STATUS,
                            description  = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.INTERNAL_SERVER_ERROR_STATUS,
                            description  = "Internal Server Error"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.SERVICE_UNAVAILABLE_STATUS,
                            description  = "Service Unavailable"
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BmsCustomerResponseDto create(@Valid @RequestBody CreateCustomerRequestDTO customerAccountOpeningCreateRequestDTO);

    @Operation(
            summary   = "Retrieve a personal account opening request by CPF",
            responses = {
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.OK_STATUS,
                            description  = "Ok"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.BAD_REQUEST_STATUS,
                            description  = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.UNAUTHORIZED_STATUS,
                            description  = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.FORBIDDEN,
                            description  = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.NOT_FOUND_STATUS,
                            description  = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.INTERNAL_SERVER_ERROR_STATUS,
                            description  = "Internal Server Error"
                    ),
                    @ApiResponse(
                            responseCode = OpenAPIResponseCodes.SERVICE_UNAVAILABLE_STATUS,
                            description  = "Service Unavailable"
                    )
            }
    )
    @GetMapping("/{customerCpf}")
    @ResponseStatus(HttpStatus.OK)
    BmsCustomerResponseDto findOwner(@PathVariable String customerCpf);
}
