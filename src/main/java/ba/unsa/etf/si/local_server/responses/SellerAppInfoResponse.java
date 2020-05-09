package ba.unsa.etf.si.local_server.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerAppInfoResponse {

    private Long officeId;
    private Long businessId;
    private String businessName;
    private String language;
    private boolean restaurant;
    private  String placeName;
}
