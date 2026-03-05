package wg.mgr.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wg.mgr.backend.model.VpnServer;

public interface VpnServerRepository extends JpaRepository<VpnServer, Long> {
}
