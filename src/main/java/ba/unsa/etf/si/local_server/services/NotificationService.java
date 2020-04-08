package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.repositories.NotificationRepository;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
}
