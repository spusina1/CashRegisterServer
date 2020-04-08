package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.Notification;
import ba.unsa.etf.si.local_server.repositories.NotificationRepository;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.requests.SellerAppRequest;
import ba.unsa.etf.si.local_server.requests.SendNotificationRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public String saveNotification(SendNotificationRequest notificationRequest) {
        if(notificationRequest != null){
            Notification newNotification = new Notification();
            newNotification.setMessage(notificationRequest.getMessage());
            notificationRepository.save(newNotification);
            return "Notification is successfully saved!";
        }
        return "";
    }

    public List<Notification> getNotifications(Long id) {
        return notificationRepository
                .findAll()
                .stream()
                .filter(n -> n.getId() > id)
                .collect(Collectors.toList());
    }

}
