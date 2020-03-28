package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.Role;
import ba.unsa.etf.si.local_server.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
