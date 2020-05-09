package ba.unsa.etf.si.local_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Table(name = "tables")
public class Table {
    @Id
    private Long id;
    private String tableName;
}
