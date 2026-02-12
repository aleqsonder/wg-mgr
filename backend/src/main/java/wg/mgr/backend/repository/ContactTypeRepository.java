package wg.mgr.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wg.mgr.backend.model.ContactType;

public interface ContactTypeRepository extends JpaRepository<ContactType, Long> {
}
