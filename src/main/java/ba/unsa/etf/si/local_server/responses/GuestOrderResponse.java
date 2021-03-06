package ba.unsa.etf.si.local_server.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class GuestOrderResponse {
    private Long id;
    private String message;
    private boolean served;
    private boolean seen;
    private Set<SellerAppReceiptItemsResponse> receiptItems;

}
