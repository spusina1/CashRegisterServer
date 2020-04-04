package ba.unsa.etf.si.local_server.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceiptResponse {
    private final Integer statusCode = 200;
    private String message;
}
