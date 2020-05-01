package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Role;
import ba.unsa.etf.si.local_server.models.RoleName;
import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.repositories.RoleRepository;
import ba.unsa.etf.si.local_server.repositories.UserRepository;
import ba.unsa.etf.si.local_server.requests.*;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import ba.unsa.etf.si.local_server.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.*;

@AllArgsConstructor
@Service
public class UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final MainPasswordService mainPasswordService;


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
                "no phone number",
                "",
                false);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> userRoles = Collections.singletonList(new Role(0L, RoleName.ROLE_GUEST));
        user.setRoles(new HashSet<>(userRoles));

        return user;
    }

    public String generateResetToken(GetResetTokenRequest getResetTokenRequest) throws MessagingException, IOException, TemplateException {

        Optional<User> user=  userRepository.findByUsername(getResetTokenRequest.getUserInfo());
        if(!user.isPresent()){
            user = userRepository.findByEmail(getResetTokenRequest.getUserInfo());
            if(!user.isPresent()){
                return "The username or email you entered doesn't belong to an account. Please check and try again!";
            }
        }
        String resetToken = UUID.randomUUID().toString();
        user.get().setResetToken(resetToken);
        userRepository.save(user.get());
        mailService.sendmail(user.get().getEmail(), user.get().getName(), resetToken);
        return "Token is sent!";

    }


    public String verifyInfo(VerifyInfoRequest verifyInfoRequest) {
        Optional<User> user=  userRepository.findByUsername(verifyInfoRequest.getUserInfo());
        if(!user.isPresent()){
            user = userRepository.findByEmail(verifyInfoRequest.getUserInfo());
            if(!user.isPresent()){
                return "Incorrect verification info!";
            }
        }
        if(user.get().getResetToken().equals(verifyInfoRequest.getResetToken())){
            return "OK";
        }

        return "Incorrect verification info!";
    }

    public String changePassword(SavePasswordRequest savePasswordRequest) {

        Optional<User> user=  userRepository.findByUsername(savePasswordRequest.getUserInfo());
        if(!user.isPresent()){
            user = userRepository.findByEmail(savePasswordRequest.getUserInfo());
            if(!user.isPresent()){
                return "The username or email you entered doesn't belong to an account. Please check and try again!";
            }
        }
        user.get().setPassword(passwordEncoder.encode(savePasswordRequest.getNewPassword()));
        user.get().setResetToken("");
        user.get().setOtp(false);
        userRepository.save(user.get());
        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(user.get().getUsername(), user.get().getPassword());
        mainPasswordService.postToMain(newPasswordRequest);

     return "Password successfully changed!";
    }
}
