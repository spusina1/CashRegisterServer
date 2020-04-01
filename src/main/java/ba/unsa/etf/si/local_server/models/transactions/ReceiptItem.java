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
    private Long id;

    private Double quantity;

    @ManyToOne
    @JoinColumn(name="receipt_id", nullable=false)
    private Receipt receipt;

}

