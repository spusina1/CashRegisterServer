package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.responses.CashRegisterResponse;
import ba.unsa.etf.si.local_server.services.CashRegisterService;
import ba.unsa.etf.si.local_server.services.MainSyncUpService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MainSyncUpController {
    private final MainSyncUpService mainSyncUpService;
    private final CashRegisterService cashRegisterService;

    @Secured("ROLE_OFFICEMAN")
    @PostMapping("/sync")
    public ResponseEntity<?> syncDatabases() {
        mainSyncUpService.syncDatabases();
        return ResponseEntity.ok("Synced...");
    }

    @Secured("ROLE_OFFICEMAN")
    @GetMapping("/register")
    public ResponseEntity<?> obtainIds() {
        CashRegisterResponse response = cashRegisterService.registerCashRegister();
        return ResponseEntity.ok(response);
    }

}
