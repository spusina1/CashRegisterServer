package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
