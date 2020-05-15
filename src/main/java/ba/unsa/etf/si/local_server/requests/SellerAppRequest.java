package ba.unsa.etf.si.local_server.requests;

import ba.unsa.etf.si.local_server.models.transactions.ReceiptItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerAppRequest {
    private String message;
    private boolean served;
    private boolean seen;
    private List<ReceiptItemRequest> receiptItems;
}