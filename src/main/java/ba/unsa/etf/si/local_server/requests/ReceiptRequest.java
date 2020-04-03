package ba.unsa.etf.si.local_server.requests;

import ba.unsa.etf.si.local_server.models.transactions.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ReceiptRequest {
    private String receiptId;
    private String username;
    private Long cashRegisterId;
    private String paymentMethod;
    private Set<ReceiptItemRequest> receiptItems;
}
