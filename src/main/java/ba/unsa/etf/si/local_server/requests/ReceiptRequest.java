package ba.unsa.etf.si.local_server.requests;

import ba.unsa.etf.si.local_server.models.transactions.ReceiptItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.util.Set;

@Data
@AllArgsConstructor
public class ReceiptRequest {
    private Long id;
    private  String username;
    private  Long cashRegisterId;
    private Set<ReceiptItemRequest> receiptItems;

}
