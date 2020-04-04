package ba.unsa.etf.si.local_server.models.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    private Long id;
  
    @JsonProperty("id")
    private Long productId;
  
    private Double quantity;

}