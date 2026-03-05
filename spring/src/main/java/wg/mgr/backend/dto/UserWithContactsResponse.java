package wg.mgr.backend.dto;

import java.util.List;

public record UserWithContactsResponse(
        Long id,
        String username,
        List<ContactResponse> contacts
) {
}
