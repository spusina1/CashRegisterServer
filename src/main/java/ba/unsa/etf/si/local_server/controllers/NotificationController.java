package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.Notification;
import ba.unsa.etf.si.local_server.requests.SendNotificationRequest;
import ba.unsa.etf.si.local_server.responses.Response;
import ba.unsa.etf.si.local_server.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
public class NotificationController {
    private final NotificationService notificationService;

    @Secured({"ROLE_OFFICEMAN", "ROLE_CASHIER", "ROLE_BARTENDER", "ROLE_GUEST"})
    @PostMapping("/api/notifications")
    public ResponseEntity<Response> saveNotification(@Valid @RequestBody SendNotificationRequest notificationRequest){
        String responseMessage = notificationService.saveNotification(notificationRequest);
        return ResponseEntity.ok(new Response(responseMessage));
    }

    @Secured({"ROLE_OFFICEMAN", "ROLE_CASHIER", "ROLE_BARTENDER"})
    @GetMapping("/api/notifications/{id}")
    public List<Notification> getNotifications(@PathVariable Long id) {
        return notificationService.getNotifications(id);
    }

}
