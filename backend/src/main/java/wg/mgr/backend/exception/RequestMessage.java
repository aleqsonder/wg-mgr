package wg.mgr.backend.exception;

public record RequestMessage(
        Integer code,
        String message
) {
}
