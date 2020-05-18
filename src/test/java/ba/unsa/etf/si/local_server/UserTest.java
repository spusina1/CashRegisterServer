package ba.unsa.etf.si.local_server;

import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.repositories.UserRepository;
import ba.unsa.etf.si.local_server.requests.VerifyInfoRequest;
import ba.unsa.etf.si.local_server.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void getUserByUsernameTest(){
        User user = new User();
        user.setUsername("user1");
        user.setName("UserName");
        user.setSurname("UserSurname");

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        User user1 = userService.getUserByUsername(user.getUsername());

        assertThat(user1.getName()).isEqualTo(user.getName());
    }

    @Test
    void verifyInfoTest(){

        User user = new User();
        user.setUsername("user1");
        user.setName("UserName");
        user.setSurname("UserSurname");
        user.setPassword("123");
        user.setEmail("userEmail");
        user.setResetToken("token");

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));


        VerifyInfoRequest verifyInfoRequest = new VerifyInfoRequest();
        verifyInfoRequest.setUserInfo(user.getUsername());
        verifyInfoRequest.setResetToken(user.getResetToken());

        String response = userService.verifyInfo(verifyInfoRequest);

        assertThat(response).isEqualTo("OK");
    }
}
