package ba.unsa.etf.si.local_server.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestOrderRequest {
    private Long id;
    private List<ReceiptItemRequest> receiptItems;
}
