package wg.mgr.backend.exception;

public record ResponseMessage(
        Integer code,
        String message
) {
}
