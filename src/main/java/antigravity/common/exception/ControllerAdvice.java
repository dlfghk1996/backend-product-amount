package antigravity.common.exception;

import antigravity.common.dto.ErrorResponse;
import antigravity.common.enums.ResponseCode;
import java.sql.SQLException;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.Error;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "antigravity")
public class ControllerAdvice {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<?> handleCustom(BizException e) {

        log.info("err: {}", e.getResponseCode().getLabel());
        return new ResponseEntity<>(new ErrorResponse(e.getResponseCode()),HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        log.error("err: ", e);
        return new ResponseEntity<>(new ErrorResponse(ResponseCode.INVALID_REQUEST),HttpStatus.OK);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleENFException(EntityNotFoundException e) {
        log.error("err: ", e);
        return new ResponseEntity<>(new ErrorResponse(ResponseCode.INVALID_REQUEST),HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("err: ", e);
        return new ResponseEntity<>(new ErrorResponse(ResponseCode.ERROR),HttpStatus.OK);
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<?> handleSQLException(Exception e) {

        return new ResponseEntity<>(new ErrorResponse(ResponseCode.ERROR_SQL),HttpStatus.OK);
    }
}
