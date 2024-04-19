package antigravity.common.exception;

import antigravity.common.enums.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 맘마먹자에서 사용하는 기본 business Exception */
@Data
@EqualsAndHashCode(callSuper = false)
public class AntigravityException extends RuntimeException {

  private int errorCode;
  private ResponseCode responseCode;
  private String message;

  public AntigravityException() {
    super();
  }

  public AntigravityException(String message, ResponseCode responseCode) {
    super(message);
    this.message = message;
    this.errorCode = errorCode;
    this.responseCode = responseCode;
  }

  public AntigravityException(ResponseCode responseCode) {
    super(responseCode.getLabel());
    this.errorCode = responseCode.getCode();
    this.responseCode = responseCode;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public static class NotFoundException extends AntigravityException {}
}
