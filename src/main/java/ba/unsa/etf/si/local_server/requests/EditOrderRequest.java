package ba.unsa.etf.si.local_server.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EditOrderRequest {
    private Long id;
    private boolean served;
    private boolean seen;
    private List<ReceiptItemRequest> receiptItems;
}
