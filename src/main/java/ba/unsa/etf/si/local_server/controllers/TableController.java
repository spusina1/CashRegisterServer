package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.services.TableService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class TableController {
    private final TableService tableService;

}
