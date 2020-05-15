package ba.unsa.etf.si.local_server.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class SellerAppReceiptsResponse {
    private Long id;
    private Set<SellerAppReceiptItemsResponse> receiptItems;
}

