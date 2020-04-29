package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.requests.LoginRequest;
import ba.unsa.etf.si.local_server.responses.LoginResponse;
import ba.unsa.etf.si.local_server.responses.Response;
import ba.unsa.etf.si.local_server.security.CurrentUser;
import ba.unsa.etf.si.local_server.security.UserPrincipal;
import ba.unsa.etf.si.local_server.services.UserService;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt = userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @Secured({"ROLE_OFFICEMAN", "ROLE_CASHIER", "ROLE_BARTENDER"})
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.getUserByUsername(userPrincipal.getUsername());
        return ResponseEntity.ok(user);
    }

}
