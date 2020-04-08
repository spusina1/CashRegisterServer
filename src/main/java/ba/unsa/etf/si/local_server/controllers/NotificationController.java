package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.requests.SellerAppRequest;
import ba.unsa.etf.si.local_server.requests.SendNotificationRequest;
import ba.unsa.etf.si.local_server.responses.SaveNotificationResponse;
import ba.unsa.etf.si.local_server.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/api/notifications")
    public ResponseEntity<?> saveNotification(@Valid @RequestBody SendNotificationRequest notificationRequest){
        String responseMessage = notificationService.saveNotification(notificationRequest);
        return ResponseEntity.ok(new SaveNotificationResponse(responseMessage));
    }
}
