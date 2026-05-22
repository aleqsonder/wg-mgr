package wg.mgr.backend.modular;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import wg.mgr.backend.dto.ContactTypeExistsValidator;
import wg.mgr.backend.repository.ContactTypeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactTypeExistsValidatorTest {

    @Mock
    private ContactTypeRepository contactTypeRepository;

    @Mock
    private ConstraintValidatorContext context;

    private ContactTypeExistsValidator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ContactTypeExistsValidator(contactTypeRepository);
    }

    // ---------- null value ----------

    @Test
    void isValid_shouldReturnTrue_whenValueIsNull() {
        boolean result = validator.isValid(null, context);
        assertThat(result).isTrue();
        verifyNoInteractions(contactTypeRepository);
    }

    // ---------- existing typename ----------

    @Test
    void isValid_shouldReturnTrue_whenTypenameExists() {
        when(contactTypeRepository.existsByTypename("EMAIL")).thenReturn(true);

        boolean result = validator.isValid("EMAIL", context);

        assertThat(result).isTrue();
        verify(contactTypeRepository).existsByTypename("EMAIL");
    }

    @Test
    void isValid_shouldReturnFalse_whenTypenameDoesNotExist() {
        when(contactTypeRepository.existsByTypename("UNKNOWN")).thenReturn(false);

        boolean result = validator.isValid("UNKNOWN", context);

        assertThat(result).isFalse();
        verify(contactTypeRepository).existsByTypename("UNKNOWN");
    }

}
