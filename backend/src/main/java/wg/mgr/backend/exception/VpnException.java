package wg.mgr.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class VpnException extends Exception {
    protected final HttpStatus status;

    public VpnException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
