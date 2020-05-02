package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.Table;
import ba.unsa.etf.si.local_server.repositories.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class TableService {
    private final TableRepository tableRepository;

    public List<Table> getTables() {
        return new ArrayList<>(tableRepository
                .findAll());
    }

    public void batchInsertTables(List<Table> tables) {
        tables.forEach(table ->
                tableRepository.saveTable(table.getId(), table.getTableNumber())
        );
        tableRepository.flush();
    }
}
