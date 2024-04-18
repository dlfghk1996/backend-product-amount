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
  private Object extra;
  private String overwriteMessage;

  public AntigravityException() {
    super();
  }

  public AntigravityException(String message, int errorCode, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public AntigravityException(String message, Throwable cause) {
    super(message, cause);
    this.errorCode = ResponseCode.UNKNOWN.getCode();
  }

  public AntigravityException(String message, int errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public AntigravityException(String message) {
    super(message);
    this.errorCode = ResponseCode.UNKNOWN.getCode();
  }

  public AntigravityException(ResponseCode responseCode) {
    super(responseCode.getLabel());
    this.errorCode = responseCode.getCode();
    this.responseCode = responseCode;
  }

  public AntigravityException(String message, ResponseCode responseCode) {
    super(message);
    this.errorCode = responseCode.getCode();
    this.responseCode = responseCode;
  }

  public AntigravityException(String message, ResponseCode responseCode, Object extra) {
    super(message);
    this.errorCode = responseCode.getCode();
    this.extra = extra;
    this.responseCode = responseCode;
  }

  public AntigravityException(Throwable cause) {
    super(cause);
  }

  public int getErrorCode() {
    return errorCode;
  }

  public static class NotFoundException extends AntigravityException {}
}
