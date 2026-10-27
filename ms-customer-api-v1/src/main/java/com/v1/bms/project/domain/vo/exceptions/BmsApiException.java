package com.v1.bms.project.domain.vo.exceptions;

import org.springframework.http.HttpStatusCode;

public class BmsApiException extends RuntimeException {

    private final HttpStatusCode status;
    private final String responseBody;

    public BmsApiException(HttpStatusCode status, String responseBody) {
        super(responseBody);
        this.status = status;
        this.responseBody = responseBody;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
