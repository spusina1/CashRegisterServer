package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.requests.LoginRequest;
import ba.unsa.etf.si.local_server.responses.LoginResponse;
import ba.unsa.etf.si.local_server.security.CurrentUser;
import ba.unsa.etf.si.local_server.security.UserPrincipal;
import ba.unsa.etf.si.local_server.services.MainSyncUpService;
import ba.unsa.etf.si.local_server.services.UserService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final MainSyncUpService mainSyncUpService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt = userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.getUserByUsername(userPrincipal.getUsername());
        return ResponseEntity.ok(user);
    }

    @Secured("ROLE_OFFICEMAN")
    @PostMapping("/sync")
    public ResponseEntity<?> syncDatabases() {
        mainSyncUpService.syncDatabases();
        return ResponseEntity.ok("Synced...");
    }

}
