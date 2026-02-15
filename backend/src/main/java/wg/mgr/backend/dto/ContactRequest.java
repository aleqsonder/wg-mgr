package wg.mgr.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactRequest(

        @JsonProperty(required = true)
        @NotNull(message = "Contact type is required")
        @ContactTypeExists
        String contactType,

        @JsonProperty(required = true)
        @NotNull(message = "Contact content is required")
        @NotBlank
        String content
) {
}
