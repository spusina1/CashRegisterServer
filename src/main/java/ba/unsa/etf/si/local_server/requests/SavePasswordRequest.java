package ba.unsa.etf.si.local_server.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavePasswordRequest {
    private String userInfo;
    private String newPassword;
}
