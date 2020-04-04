package ba.unsa.etf.si.local_server.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptItemRequest {
    private Long id;
    private Double quantity;
}