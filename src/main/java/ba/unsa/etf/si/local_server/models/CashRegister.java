package ba.unsa.etf.si.local_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cash_registers")
public class CashRegister {
    @Id
    private Long id;
    private String name;
    private String uuid;
    private boolean taken;
    private boolean open;
}
