package ba.unsa.etf.si.local_server.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveNotificationResponse {
    private String message;
    private final Integer statusCode = 200;
}
