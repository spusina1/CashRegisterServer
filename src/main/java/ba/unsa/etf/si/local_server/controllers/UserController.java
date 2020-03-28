package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.requests.LoginRequest;
import ba.unsa.etf.si.local_server.responses.LoginResponse;
import ba.unsa.etf.si.local_server.security.CurrentUser;
import ba.unsa.etf.si.local_server.security.UserPrincipal;
import ba.unsa.etf.si.local_server.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/test")
    public String result() {
        return "The Greatest Secret Ever!";
    }

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

}
