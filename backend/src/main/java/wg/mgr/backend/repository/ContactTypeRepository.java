package wg.mgr.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wg.mgr.backend.model.ContactType;

import java.util.Optional;

public interface ContactTypeRepository extends JpaRepository<ContactType, Long> {
    boolean existsByTypename(String typename);

    Optional<ContactType> findByTypename(String s);
}
