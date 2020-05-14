package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.responses.Response;
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

    // TODO: Implement login with office server account
    @PostMapping("/sync")
    public ResponseEntity<Response> syncDatabases() {
        mainSyncUpService.syncDatabases();
        return ResponseEntity.ok(new Response("Synced..."));
    }

    @GetMapping("/test")
    public ResponseEntity<Response> testRoute() {
        return ResponseEntity.ok(new Response("This is a test route!"));
    }

}
