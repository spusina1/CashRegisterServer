package ba.unsa.etf.si.local_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String receiptId;

    @NotBlank
    private   Long cashRegisterId;

    @NotBlank
    private Long officeId;

    @NotBlank
    private  Long businessId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "receipt_receiptItem",
            joinColumns = @JoinColumn(name = "receipt_id"),
            inverseJoinColumns = @JoinColumn(name = "receiptItem_id"))

    private Set<ReceiptItem> receiptItems = new HashSet<>();

    @NotBlank
    private String username;

    @NotBlank
    private  BigDecimal totalPrice;

    @NotBlank
    private Long timestamp;
}
