package wg.mgr.backend.exception;

import org.springframework.http.HttpStatus;

public class ConflictVpnException extends VpnRuntimeException {
    public ConflictVpnException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
