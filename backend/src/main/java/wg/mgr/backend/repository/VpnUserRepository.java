package wg.mgr.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wg.mgr.backend.model.VpnUser;

public interface VpnUserRepository extends JpaRepository<VpnUser, Long> {
}
