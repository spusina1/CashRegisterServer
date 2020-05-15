package ba.unsa.etf.si.local_server.models.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "receipts", uniqueConstraints = {
        @UniqueConstraint(columnNames = "receiptId")
})
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private String receiptId;

    @JsonProperty("status")
    private ReceiptStatus receiptStatus;

    private PaymentMethod paymentMethod;

    private Long cashRegisterId;

    private Long officeId;

    private Long businessId;

    @OneToMany(cascade =  CascadeType.ALL)
    @JoinColumn(name = "receipt_id")
    private Set<ReceiptItem> receiptItems;

    private String username;

    private BigDecimal totalPrice;

    private Long timestamp;

    private String message;

    private boolean served;

    private boolean seen;

}