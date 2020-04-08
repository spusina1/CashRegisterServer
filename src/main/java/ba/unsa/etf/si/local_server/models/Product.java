package ba.unsa.etf.si.local_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    private Double quantity;

    private BigDecimal price;

    private Integer discount;

    @NotBlank
    private String measurementUnit;

    @Column(columnDefinition = "TEXT")
    private String imageBase64;

    private String barcode;

    private String description;

}
