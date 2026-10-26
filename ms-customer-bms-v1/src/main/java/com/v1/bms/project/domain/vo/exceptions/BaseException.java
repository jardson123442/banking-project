package com.v1.bms.project.domain.vo.exceptions;

import java.io.Serial;

public class BaseException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -597564568216657091L;

  public BaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public BaseException(Throwable cause) {
    super(cause);
  }

  public BaseException(String message) {
    super(message);
  }

  public BaseException() {

  }
}
