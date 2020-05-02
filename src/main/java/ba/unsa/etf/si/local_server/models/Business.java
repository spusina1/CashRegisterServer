package ba.unsa.etf.si.local_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "business")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String businessName;
    private boolean restaurant;
    private String language;
    private String startTime;
    private String endTime;
    private String syncTime;

    @OneToMany(cascade =  CascadeType.ALL)
    @JoinColumn(name = "bussines_id")
    private List<CashRegister> cashRegisters;
}
