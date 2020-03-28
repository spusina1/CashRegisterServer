package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}
