package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.respositories.UserRepository;
import lombok.AllArgsConstructor;
import ba.unsa.etf.si.local_server.requests.LoginRequest;
import ba.unsa.etf.si.local_server.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public String authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    public User getUserByUsername(String username) {
        String errorMessage = String.format("No user with username '%s'", username);
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage));
    }

}
