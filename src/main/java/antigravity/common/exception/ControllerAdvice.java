package antigravity.common.exception;

import antigravity.common.enums.ResponseCode;
import antigravity.common.model.Response;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "antigravity")
public class ControllerAdvice {

  @ExceptionHandler(AntigravityException.class)
  public Response<Object> handleCustom(AntigravityException ce) {
    Response<Object> response = new Response<>(null, ce.getResponseCode());
    if (ce.getExtra() != null) {
      response.setData(ce.getExtra());
    }

    if (ce.getResponseCode() == ResponseCode.ERROR) {
      log.error("err: ", ce);
    } else {
      log.info("err: {}", ce.getResponseCode().getLabel());
    }

    if (ce.getOverwriteMessage() != null) {
      response.setMessage(ce.getOverwriteMessage());
    }

    return response;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Response<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("err: ", e);
    return new Response<>(null, ResponseCode.INVALID_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public Response<Object> handleException(Exception e) {
    log.error("err: ", e);
    return new Response<>(null, ResponseCode.ERROR);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public Response<Object> handleENFException(EntityNotFoundException e) {
    log.error("err: ", e);
    return new Response<>(null, ResponseCode.ERROR_NO_MATCH_REQUEST_PARAM);
  }
}
