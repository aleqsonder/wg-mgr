package wg.mgr.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class VpnRuntimeException extends RuntimeException {
  protected final HttpStatus status;

  public VpnRuntimeException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
