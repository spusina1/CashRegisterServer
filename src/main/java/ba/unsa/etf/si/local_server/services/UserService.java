package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Role;
import ba.unsa.etf.si.local_server.models.RoleName;
import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.repositories.RoleRepository;
import ba.unsa.etf.si.local_server.repositories.UserRepository;
import lombok.AllArgsConstructor;
import ba.unsa.etf.si.local_server.requests.LoginRequest;
import ba.unsa.etf.si.local_server.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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

    public void batchInsertUsers(List<User> users) {
        Long guestId = users
                .stream()
                .map(User::getId)
                .min(Comparator.comparingLong(id -> id))
                .orElse(1L) - 1;

        userRepository.deleteAllInBatch();
        userRepository.flush();
        roleRepository.deleteAllInBatch();
        roleRepository.flush();
        userRepository.saveAll(users);
        userRepository.save(createGuestUser(guestId));
        userRepository.flush();
    }

    public User createGuestUser(Long guestId) {
        User user = new User(
                guestId,
                "guest",
                "password",
                "guest@mail.com",
                null,
                "Guest",
                "User",
                "no address",
                "no city",
                "no country",
                "no phone number");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> userRoles = Collections.singletonList(new Role(0L, RoleName.ROLE_GUEST));
        user.setRoles(new HashSet<>(userRoles));

        return user;
    }
}
