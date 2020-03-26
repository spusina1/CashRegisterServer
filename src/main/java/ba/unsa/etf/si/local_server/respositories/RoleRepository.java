package ba.unsa.etf.si.local_server.respositories;

import ba.unsa.etf.si.local_server.models.Role;
import ba.unsa.etf.si.local_server.models.RoleName;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(RoleName roleName);
}
