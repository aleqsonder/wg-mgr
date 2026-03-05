package wg.mgr.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wg.mgr.backend.model.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
