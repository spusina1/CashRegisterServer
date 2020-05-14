package ba.unsa.etf.si.local_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
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

    private Double pdv;

    @ManyToOne
    private ItemType itemType;

    @OneToMany(mappedBy = "product")
    private List<ProductItem> productItems;

}
