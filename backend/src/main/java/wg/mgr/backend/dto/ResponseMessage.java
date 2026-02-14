package wg.mgr.backend.dto;

public record ResponseMessage(
        Integer code,
        String message
) {
}
