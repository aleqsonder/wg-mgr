package wg.mgr.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wg.mgr.backend.model.Client;

public interface UserRepository extends JpaRepository<Client, Long> {
    // has standard data access methods by default
    // we can add custom methods here (also with native SQL queries)
}
