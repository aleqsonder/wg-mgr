package wg.mgr.backend.modular;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import wg.mgr.backend.dto.ResponseMessage;
import wg.mgr.backend.exception.NotFoundVpnException;
import wg.mgr.backend.exception.VpnControllerAdvice;
import wg.mgr.backend.exception.VpnRuntimeException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VpnControllerAdviceTest {

    private VpnControllerAdvice advice;

    @BeforeEach
    void setUp() {
        this.advice = new VpnControllerAdvice();
    }

    // ---------- handleVpnRuntimeException ----------

    @Test
    void handleVpnRuntimeException_shouldReturnProperResponse() {
        VpnRuntimeException ex = new NotFoundVpnException("ERR");
        ResponseEntity<?> response = advice.handleVpnRuntimeException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ResponseMessage body = (ResponseMessage) response.getBody();
        assert body != null;
        assertThat(body.code()).isEqualTo( HttpStatus.NOT_FOUND.value());
        assertThat(body.message()).isEqualTo("ERR");
    }

    // ---------- handleMissingFields ----------

    @Test
    void handleMissingFields_shouldReturnBadRequest() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                "JSON parse error",
                new NullPointerException(),
                new MockHttpInputMessage(new byte[]{})
        );
        ResponseEntity<?> response = advice.handleMissingFields(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ResponseMessage body = (ResponseMessage) response.getBody();
        assert body != null;
        assertThat(body.code()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.message()).contains("JSON parse error");
    }

    // ---------- handleInvalidDTO ----------

    @Test
    void handleInvalidDTO_shouldReturnUnprocessableEntity() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("obj", "field1", "must not be null"),
                new FieldError("obj", "field2", "invalid format")
        ));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<?> response = advice.handleInvalidDTO(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
        ResponseMessage body = (ResponseMessage) response.getBody();
        assert body != null;
        assertThat(body.code()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT.value());
        assertThat(body.message()).contains("must not be null");
        assertThat(body.message()).contains("invalid format");
    }

    // ---------- handleConstraintViolation ----------

    @Test
    void handleConstraintViolation_shouldReturnUnprocessableEntity() {
        ConstraintViolation<?> v1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> v2 = mock(ConstraintViolation.class);
        when(v1.getMessage()).thenReturn("field1 invalid");
        when(v2.getMessage()).thenReturn("field2 invalid");
        ConstraintViolationException ex = new ConstraintViolationException(Set.of(v1, v2));
        ResponseEntity<?> response = advice.handleConstraintViolation(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
        ResponseMessage body = (ResponseMessage) response.getBody();
        assert body != null;
        assertThat(body.code()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT.value());
        assertThat(body.message()).contains("field1 invalid");
        assertThat(body.message()).contains("field2 invalid");
    }

    // ---------- handleException ----------

    @Test
    void handleException_shouldReturnInternalServerError() {
        Exception ex = new Exception("unexpected error");
        ResponseEntity<?> response = advice.handleException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseMessage body = (ResponseMessage) response.getBody();
        assert body != null;
        assertThat(body.code()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(body.message()).isEqualTo("unexpected error");
    }

}
