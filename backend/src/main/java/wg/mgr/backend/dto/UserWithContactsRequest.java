package wg.mgr.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Objects;

public record UserWithContactsRequest(

        @JsonProperty(required = true)
        @NotNull(message = "Username is required")
        @Pattern(regexp = "^[a-zA-Z0-9_-]{3,20}$", message = "Invalid username format")
        String username,

        @Valid
        List<ContactRequest> contacts
) {
    public UserWithContactsRequest {
        contacts = Objects.requireNonNullElseGet(contacts, List::of);
    }
}
