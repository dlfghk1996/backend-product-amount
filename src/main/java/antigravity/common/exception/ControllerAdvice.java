package antigravity.common.exception;

import antigravity.common.enums.ResponseCode;
import java.sql.SQLException;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "antigravity")
public class ControllerAdvice {

  @ExceptionHandler(AntigravityException.class)
  public ResponseEntity<?> handleCustom(AntigravityException e) {

    log.info("err: {}", e.getResponseCode().getLabel());

    return ResponseEntity
        .status(e.getResponseCode().getCode())
        .body(e.getMessage()==null?e.getResponseCode().getLabel():e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("err: ", e);
    return ResponseEntity
        .status(ResponseCode.INVALID_REQUEST.getCode())
        .body(ResponseCode.INVALID_REQUEST.getLabel());
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> handleENFException(EntityNotFoundException e) {
    log.error("err: ", e);
    return ResponseEntity
        .status(ResponseCode.INVALID_REQUEST.getCode())
        .body(ResponseCode.INVALID_REQUEST.getLabel());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Exception e) {
    log.error("err: ", e);
    return ResponseEntity
        .status(ResponseCode.ERROR.getCode())
        .body(ResponseCode.ERROR.getLabel());
  }

  @ExceptionHandler({SQLException.class, DataAccessException.class})
  public ResponseEntity<?> handleSQLException(Exception e){
//    ErrorResponse errorResponse = new ErrorResponse(500, "DB 접속 오류가 발생했습니다. DB정보를 다시 확인해주세요.");
//    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    return ResponseEntity
        .status(ResponseCode.ERROR_SQL.getCode())
        .body(ResponseCode.ERROR_SQL.getLabel());
  }
}
