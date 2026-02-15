package wg.mgr.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.MismatchedInputException;
import wg.mgr.backend.dto.ResponseMessage;

import java.util.Arrays;

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

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<?> handleMissingFields(MismatchedInputException e) {
        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.BAD_REQUEST.value(),
                e.getOriginalMessage()
        );
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.info(Arrays.toString(e.getStackTrace()));
        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage()
        );
        return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
