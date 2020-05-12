package ba.unsa.etf.si.local_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@javax.persistence.Table(name = "business")
public class Business {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = 1L;

    private Long businessId;
    private Long officeId;
    private String businessName;
    private boolean restaurant;
    private String language;
    private String startTime;
    private String endTime;
    private String syncTime;
    private String placeName;

    @OneToMany(cascade =  CascadeType.ALL)
    @JoinColumn(name = "business_id")
    private List<CashRegister> cashRegisters;
}
