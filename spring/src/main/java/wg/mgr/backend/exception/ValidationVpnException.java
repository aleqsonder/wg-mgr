package wg.mgr.backend.exception;

import org.springframework.http.HttpStatus;

public class ValidationVpnException extends VpnRuntimeException {
    public ValidationVpnException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
