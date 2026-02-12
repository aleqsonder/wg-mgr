package wg.mgr.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wg.mgr.backend.model.VpnUserContact;

public interface VpnUserContactRepository extends JpaRepository<VpnUserContact, Long> {
}
