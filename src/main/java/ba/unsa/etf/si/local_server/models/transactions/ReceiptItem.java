package ba.unsa.etf.si.local_server.models.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "receiptItems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long productId;
    private Double quantity;

}