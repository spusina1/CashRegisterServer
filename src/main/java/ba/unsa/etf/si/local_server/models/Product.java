package ba.unsa.etf.si.local_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.awt.*;

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
    private String title;

    //private Image image;
    private Integer quantity;
    @NotBlank
    private Float price;
    private Float discount;

    @ManyToOne
    @JoinColumn
    private Branch branch;




}
