package ba.unsa.etf.si.local_server.models.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

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
    @JsonView(Views.Public.class)
    private Long id;

    private String receiptId;

    private  ReceiptStatus receiptStatus;

    private   Long cashRegisterId;

    private Long officeId;

    private  Long businessId;

    @OneToMany(cascade =  CascadeType.ALL)
    @JoinColumn(name = "receipt_id")
    private Set<ReceiptItem> receiptItems = new HashSet<>();
  
    private String username;

    private  BigDecimal totalPrice;


    private Long timestamp;

}