package com.v1.bms.project.application.port.output;

import com.v1.bms.project.domain.vo.exceptions.BaseException;

public class CustomerCpfAlreadyInUseException extends BaseException {
    public CustomerCpfAlreadyInUseException(String message) {
        super(message);
    }
}
