package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.Notification;
import ba.unsa.etf.si.local_server.models.Table;
import ba.unsa.etf.si.local_server.services.TableService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class TableController {
    private final TableService tableService;

    @GetMapping("/api/tables")
    public List<Table> getTables() {
        return tableService.getTables();
    }
}
