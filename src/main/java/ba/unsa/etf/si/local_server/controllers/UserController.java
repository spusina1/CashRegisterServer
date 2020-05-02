package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.requests.GetResetTokenRequest;
import ba.unsa.etf.si.local_server.requests.LoginRequest;
import ba.unsa.etf.si.local_server.requests.SavePasswordRequest;
import ba.unsa.etf.si.local_server.requests.VerifyInfoRequest;
import ba.unsa.etf.si.local_server.responses.LoginResponse;
import ba.unsa.etf.si.local_server.responses.Response;
import ba.unsa.etf.si.local_server.security.CurrentUser;
import ba.unsa.etf.si.local_server.security.UserPrincipal;
import ba.unsa.etf.si.local_server.services.UserService;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

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

    @PostMapping("/reset-token")
    public ResponseEntity<Response> getResetToken(@Valid @RequestBody GetResetTokenRequest getResetTokenRequest) throws MessagingException, IOException, TemplateException {
        String message = userService.generateResetToken(getResetTokenRequest);
        return ResponseEntity.ok(new Response(message));
    }

    @PostMapping("/verification-info")
    public ResponseEntity<Response> verifyInfo(@Valid @RequestBody VerifyInfoRequest verifyInfoRequest) throws MessagingException {
        String message = userService.verifyInfo(verifyInfoRequest);
        return ResponseEntity.ok(new Response(message));
    }

    @PutMapping("/password")
    public ResponseEntity<Response> savePassword(@Valid @RequestBody SavePasswordRequest savePasswordRequest) throws MessagingException {
        String message = userService.changePassword(savePasswordRequest);
        return ResponseEntity.ok(new Response(message));
    }

}
