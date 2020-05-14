package ba.unsa.etf.si.local_server.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainLoginRequest {
    private String username;
    private String password;
    private final String role = "ROLE_SERVER";
}
