package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.requests.LoginRequest;
import ba.unsa.etf.si.local_server.responses.Response;
import ba.unsa.etf.si.local_server.services.MainSyncUpService;
import ba.unsa.etf.si.local_server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MainSyncUpController {
    private final MainSyncUpService mainSyncUpService;
    private final UserService userService;

    @PostMapping("/sync")
    public ResponseEntity<Response> syncDatabases(@Valid @RequestBody LoginRequest loginRequest) {
        if(userService.isServerUser(loginRequest)) {
            mainSyncUpService.syncDatabases();
            return ResponseEntity.ok(new Response("Synced..."));
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new Response("You have no privilege to do this!"));
    }

    @GetMapping("/test")
    public ResponseEntity<Response> testRoute() {
        return ResponseEntity.ok(new Response("This is a test route!"));
    }

}
