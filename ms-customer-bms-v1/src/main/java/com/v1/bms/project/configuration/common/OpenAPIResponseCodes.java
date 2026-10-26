package com.v1.bms.project.configuration.common;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OpenAPIResponseCodes {

    public static final String OK_STATUS = "200";
    public static final String CREATED_STATUS = "201";
    public static final String ACCEPTED_STATUS = "202";
    public static final String NO_CONTENT = "204";
    public static final String BAD_REQUEST_STATUS = "400";
    public static final String UNAUTHORIZED_STATUS = "401";
    public static final String CONFLICT = "409";
    public static final String INTERNAL_SERVER_ERROR_STATUS = "500";
    public static final String SERVICE_UNAVAILABLE_STATUS = "503";
    public static final String NOT_FOUND_STATUS = "404";
    public static final String UNPROCESSABLE_ENTITY_STATUS = "422";
    public static final String TOO_MANY_REQUESTS = "429";
    public static final String FORBIDDEN = "403";
}
