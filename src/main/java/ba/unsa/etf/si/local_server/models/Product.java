package ba.unsa.etf.si.local_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @Positive
    private Double quantity;

    @Positive
    private BigDecimal price;

    @Positive
    private Integer discount;

    @NotBlank
    private String measurementUnit;

    @NotBlank
    private String imageBase64;

    @ManyToOne
    @JoinColumn
    private Branch branch;
}
