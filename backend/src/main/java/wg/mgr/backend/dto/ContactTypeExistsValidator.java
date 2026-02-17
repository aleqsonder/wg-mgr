package wg.mgr.backend.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import wg.mgr.backend.repository.ContactTypeRepository;

@Component
public class ContactTypeExistsValidator implements ConstraintValidator<ContactTypeExists, String> {

    private final ContactTypeRepository contactTypeRepository;

    public ContactTypeExistsValidator(ContactTypeRepository contactTypeRepository) {
        this.contactTypeRepository = contactTypeRepository;
    }

    @Override
    public boolean isValid(String typename, ConstraintValidatorContext context) {
        if (typename == null) {
            return true; // check for null with @NotNull
        }
        return contactTypeRepository.existsByTypename(typename);
    }
}
