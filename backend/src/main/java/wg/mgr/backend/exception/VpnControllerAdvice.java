package wg.mgr.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wg.mgr.backend.dto.ResponseMessage;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage()
        );
        return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
