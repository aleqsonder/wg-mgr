package wg.mgr.backend.exception;

import org.springframework.http.HttpStatus;

public class ServerVpnException extends VpnRuntimeException {
    public ServerVpnException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
