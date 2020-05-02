package ba.unsa.etf.si.local_server.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyInfoRequest {
    private String userInfo;
    private String resetToken;
}
