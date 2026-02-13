package wg.mgr.backend.exception;

import org.springframework.http.HttpStatus;

public class NotFoundVpnException extends VpnRuntimeException {
    public NotFoundVpnException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
