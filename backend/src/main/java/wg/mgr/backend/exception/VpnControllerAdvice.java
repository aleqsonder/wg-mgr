package wg.mgr.backend.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wg.mgr.backend.dto.ResponseMessage;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class VpnControllerAdvice {

    @ExceptionHandler(VpnRuntimeException.class)
    public ResponseEntity<?> handleVpnRuntimeException(VpnRuntimeException e) {
        ResponseMessage responseMessage = new ResponseMessage(
                e.getStatus().value(),
                e.getMessage()
        );
        return new ResponseEntity<>(responseMessage, e.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleMissingFields(HttpMessageNotReadableException e) {
        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.BAD_REQUEST.value(),
                e.getLocalizedMessage()
        );
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleInvalidDTO(MethodArgumentNotValidException e) {
        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                e.getBindingResult().getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining("; "))
        );
        return new ResponseEntity<>(responseMessage, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                ex.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("; "))
        );
        return new ResponseEntity<>(responseMessage, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.info(e.getClass().getName());
        log.info(e.getLocalizedMessage());
        log.info(e.getMessage());
        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage()
        );
        return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
